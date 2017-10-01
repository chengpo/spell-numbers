/*
MIT License

Copyright (c) 2017 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.monkeyapp.numbers.ocrcapture

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import com.google.android.gms.common.images.Size
import com.google.android.gms.vision.Detector
import java.nio.ByteBuffer
import java.util.*

class CameraSource(val context: Context, val detector: Detector<Any>) {
    private val TAG = "CameraSource"
    private val ASPECT_RATIO_TOLERANCE = 0.01f
    private val DEFAULT_PREVIEW_IMAGE_FORMAT = ImageFormat.NV21

    var requestedPreviewWidth: Int = 0
    var requestedPreviewHeight: Int = 0
    var requestedPreviewFps: Float = 30.0f


    private var rotation = 0
    private lateinit var previewSize: Size

    private val bytesToByteBuffer = mutableMapOf<ByteArray, ByteBuffer>()

    private data class SizePair(val preview: Size, val picture: Size? = null)

    private val rearCameraId: Int by lazy {
        val cameraInfo = Camera.CameraInfo()
        var cameraId = -1

        for (i in 0 until Camera.getNumberOfCameras()) {
            Camera.getCameraInfo(i, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i
                break
            }
        }

        cameraId
    }

    private var _camera: Camera? = null
    private val camera: Camera
        get() {
            if (_camera == null && rearCameraId != -1) {
                _camera = Camera.open(rearCameraId)

                val sizePair = selectSizePair(_camera!!)
                this.previewSize = sizePair.preview

                // set picture size
                if (sizePair.picture != null) {
                    _camera!!.parameters.setPictureSize(
                                             sizePair.picture.width,
                                             sizePair.picture.height)
                }

                // set preview size
                _camera!!.parameters.setPreviewSize(
                        sizePair.preview.width,
                        sizePair.preview.height
                )

                // set preview fps
                val fpsRange = selectPreviewFpsRange(_camera!!)
                _camera!!.parameters.setPreviewFpsRange(
                    fpsRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX],
                    fpsRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]
                )
                _camera!!.parameters.previewFormat = DEFAULT_PREVIEW_IMAGE_FORMAT

                // set rotation
                setRotation(_camera!!, _camera!!.parameters, rearCameraId)

                // set focus mode
                if (_camera!!.parameters.supportedFocusModes.contains(
                                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    _camera!!.parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                } else {
                    _camera!!.parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                }

                Log.i(TAG, "Camera focus mode = " + _camera!!.parameters.focusMode)

                // set the needed four frame buffers
                _camera!!.setPreviewCallback(CameraPreviewCallback())
                for (i in 1..4) {
                    _camera!!.addCallbackBuffer(createPreviewBuffer(this.previewSize))
                }
            }

            return _camera ?: throw IllegalStateException("Failed to find rear camera")
        }

    fun release() {
        _camera!!.release()
        _camera = null
    }

    private inner class CameraPreviewCallback : Camera.PreviewCallback {
        override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
            Log.d(TAG, "receive preview frame")
            // TODO: mFrameProcessor.setNextFrame(data, camera)
        }
    }

    // TODO: Process frame in worker thread

    private fun createPreviewBuffer(previewSize: Size): ByteArray {
        val bitsPerPixel = ImageFormat.getBitsPerPixel(DEFAULT_PREVIEW_IMAGE_FORMAT)
        val sizeInBits = previewSize.height * previewSize.width * bitsPerPixel
        val bufferSize = (Math.ceil(sizeInBits / 8.0) + 1).toInt()

        val byteArray = ByteArray(bufferSize)
        val buffer = ByteBuffer.wrap(byteArray)
        if (buffer.hasArray() && Arrays.equals(buffer.array(), byteArray)) {
            bytesToByteBuffer.put(byteArray, buffer)
            return byteArray
        }

        throw IllegalStateException("Failed to create valid buffer for camera source.")
    }

    private fun setRotation(camera: Camera, parameters: Camera.Parameters, cameraId: Int) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val degrees = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }

        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, cameraInfo)

        val angle: Int
        val displayAngle: Int

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            angle = (cameraInfo.orientation + degrees) % 360
            displayAngle = (360 - angle)
        } else {
            angle = (cameraInfo.orientation - degrees + 369) % 360
            displayAngle = angle
        }

        this.rotation = angle / 90

        camera.setDisplayOrientation(displayAngle)
        parameters.setRotation(angle)
    }

    private fun selectPreviewFpsRange(camera: Camera, requestedFps: Float = 30.0f): IntArray {
        val requestedFpsScaled = (requestedFps * 1000.0f).toInt()

        var selectedFpsRange  =camera.parameters.supportedPreviewFpsRange[0]
        var minDiff = Int.MAX_VALUE

        camera.parameters
              .supportedPreviewFpsRange
                .forEach {
                    val deltaMin = requestedFpsScaled - it[Camera.Parameters.PREVIEW_FPS_MIN_INDEX]
                    val deltaMax = requestedFpsScaled - it[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]
                    var diff = Math.abs(deltaMin) + Math.abs(deltaMax)
                    if (diff < minDiff) {
                        selectedFpsRange = it
                        minDiff = diff
                    }
                }


        return selectedFpsRange
    }


    private fun selectSizePair(camera: Camera): SizePair {
        var sizePairList = getValidPreviewSizeList(camera)
        var bestMatch = sizePairList[0]
        var minDiff = Int.MAX_VALUE

        sizePairList.forEach {
            val diff = Math.abs(it.preview.width - requestedPreviewWidth) +
                    Math.abs(it.preview.height - requestedPreviewHeight)

            if (diff < minDiff) {
                bestMatch = it
                minDiff = diff
            }
        }

        return bestMatch
    }

    private fun getValidPreviewSizeList(camera: Camera): List<SizePair> {
        val supportedPreviewSizes = camera.parameters.supportedPreviewSizes
        val supportedPictureSizes = camera.parameters.supportedPictureSizes

        val _validPreviewSizes = mutableListOf<SizePair>()

        for (previewSize in supportedPreviewSizes) {
            val previewRatio = previewSize.width.toFloat() / previewSize.height.toFloat()

            for (pictureSize in supportedPictureSizes) {
                val pictureRatio = pictureSize.width.toFloat() / pictureSize.height.toFloat()
                if (Math.abs(previewRatio - pictureRatio) < ASPECT_RATIO_TOLERANCE) {
                    _validPreviewSizes.add(
                            SizePair(Size(previewSize.width, previewSize.height),
                                    Size(pictureSize.width, pictureSize.height)))

                    break
                }
            }
        }

        if (_validPreviewSizes.isEmpty()) {
            Log.w(TAG, "No preview sizes have a corresponding same-aspect-ratio picture size")

            supportedPreviewSizes.forEach {
                _validPreviewSizes.add(SizePair(Size(it.width, it.height)))
            }
        }

        return _validPreviewSizes
    }
}