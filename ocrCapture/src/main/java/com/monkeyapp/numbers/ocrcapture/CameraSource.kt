/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

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
import android.graphics.*
import android.hardware.Camera
import android.os.SystemClock
import android.support.v8.renderscript.*
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.WindowManager
import com.google.android.gms.common.images.Size
import java.lang.Exception
import java.nio.ByteBuffer

class CameraSource(private val context: Context,
                   private val callback:CameraSource.Callback,
                   private val requestedPreviewWidth:Int = 800,
                   private val requestedPreviewHeight:Int = 640,
                   private val requestedPreviewFps:Float = 15.0f) {
    companion object {
        private const val TAG = "CameraSource"
        private const val DEFAULT_PREVIEW_IMAGE_FORMAT = ImageFormat.NV21
    }

    interface Callback {
        fun onReceiveFrameBitmap(bitmap: Bitmap, frameId:Int)
    }

    var previewSize = Size(requestedPreviewWidth, requestedPreviewHeight)

    private var rotation = 0
    private val frameProcessor = FrameProcessor()
    private var frameProcessorThread: Thread? = null

    private val cameraLock = Object()
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

                _camera?.let {
                    theCamera ->
                    val theParams = theCamera.parameters

                    theParams.previewFormat = DEFAULT_PREVIEW_IMAGE_FORMAT

                    // set preview size
                    previewSize = selectBestPreviewSize(theCamera)
                    theParams.setPreviewSize(previewSize.width, previewSize.height)
                    Log.d(TAG, "Preview Size(${previewSize.width}, ${previewSize.height})")

                    // set preview fps
                    val fpsRange = selectPreviewFpsRange(theCamera, requestedPreviewFps)
                    theParams.setPreviewFpsRange(
                            fpsRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX],
                            fpsRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]
                    )
                    Log.d(TAG, "FPS range ${fpsRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX]} - ${fpsRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]} ")

                    // set rotation
                    rotation = selectRotation(theCamera, rearCameraId)
                    theParams.setRotation(rotation)

                    // set focus mode
                    if (theCamera.parameters.supportedFocusModes.contains(
                            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        theParams.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                    } else if (theCamera.parameters.supportedFocusModes.contains(
                            Camera.Parameters.FOCUS_MODE_AUTO)){
                        theParams.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                    }

                    Log.i(TAG, "Camera focus mode = " + theParams.focusMode)

                    theCamera.parameters = theParams

                    // set the needed four frame buffers
                    theCamera.setPreviewCallback(CameraPreviewCallback())
                    for (i in 1..4) {
                        theCamera.addCallbackBuffer(createPreviewBuffer(previewSize))
                    }
                }
            }

            return _camera ?: throw IllegalStateException("Failed to find rear camera")
        }

    fun start(surfaceHolder: SurfaceHolder): CameraSource {
        synchronized(cameraLock) {
            if (_camera != null) {
                return this
            }

            camera.setPreviewDisplay(surfaceHolder)
            camera.startPreview()

            frameProcessorThread = Thread(frameProcessor)
            frameProcessor.activate = true
            frameProcessorThread?.start()
        }

        return this
    }

    fun stop() {
        synchronized(cameraLock) {
            if (_camera == null) {
                return
            }

            frameProcessor.activate = false
            try {
                frameProcessorThread?.join()
            } catch (e: InterruptedException) {
                Log.d(TAG, "Frame processor thread interrupted on stop.")
            } finally {
                frameProcessorThread = null
            }

            _camera?.run {
                try {
                    stopPreview()
                    setPreviewCallbackWithBuffer(null)
                    setPreviewDisplay(null)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to clear camera preview: " + e)
                } finally {
                    release()
                    _camera = null
                }
            }
        }
    }

    fun release() {
        synchronized(cameraLock) {
            stop()
        }
    }

    private fun createPreviewBuffer(previewSize: Size): ByteArray {
        val bitsPerPixel = ImageFormat.getBitsPerPixel(DEFAULT_PREVIEW_IMAGE_FORMAT)
        val sizeInBits = previewSize.height * previewSize.width * bitsPerPixel
        val bufferSize = (Math.ceil(sizeInBits / 8.0) + 1).toInt()

        return ByteArray(bufferSize)
    }

    private fun selectRotation(camera: Camera, cameraId: Int): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val degrees = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            else -> 270
        }

        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, cameraInfo)

        val angle: Int
        val displayAngle: Int

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            angle = (cameraInfo.orientation + degrees) % 360
            displayAngle = (360 - angle) % 360  // compensate for it being mirrored
        } else {
            angle = (cameraInfo.orientation - degrees + 360) % 360
            displayAngle = angle
        }

        camera.setDisplayOrientation(displayAngle)

        Log.d(TAG, "display orientation $displayAngle, camera rotation $angle)")
        return angle
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
                    val diff = Math.abs(deltaMin) + Math.abs(deltaMax)
                    if (diff < minDiff) {
                        selectedFpsRange = it
                        minDiff = diff
                    }
                }


        return selectedFpsRange
    }

    private fun selectBestPreviewSize(camera: Camera): Size {
        var bestMatch:Camera.Size = camera.parameters.supportedPreviewSizes[0]
        var minDiff = Int.MAX_VALUE

        camera.parameters.supportedPreviewSizes.forEach {
            val diff = Math.abs(it.width - requestedPreviewWidth) +
                    Math.abs(it.height - requestedPreviewHeight)

            if (diff < minDiff) {
                bestMatch = it
                minDiff = diff
            }
        }

        return Size(bestMatch.width, bestMatch.height)
    }

    /***************************
     * Frame processing        *
     ***************************/

    private inner class CameraPreviewCallback : Camera.PreviewCallback {
        override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
            /*Log.v(TAG, "receive preview frame: " +
                       "preview size (${camera!!.parameters.previewSize.width}, ${camera!!.parameters.previewSize.height})" +
                       "format ${camera!!.parameters.previewFormat}")*/

            frameProcessor.setNextFrame(data!!)
        }
    }

    private inner class FrameProcessor : Runnable {
        private val startTimeMillis = SystemClock.elapsedRealtime()

        private val processorLock = Object()

        private var pendingTimeMillis = 0L
        private var pendingFrameId = 0
        private var pendingFrameData: ByteBuffer? = null

        private var _activate = false
        var activate: Boolean
            get() {
                synchronized(processorLock) {
                    return _activate
                }
            }

            set(value) {
                synchronized(processorLock) {
                    _activate = value
                    processorLock.notifyAll()
                }
            }

        fun setNextFrame(data:ByteArray) {
            synchronized(processorLock) {
                if (pendingFrameData != null) {
                    // add previous unused frame buffer back to camera
                    camera.addCallbackBuffer(pendingFrameData!!.array())
                }

                pendingTimeMillis = SystemClock.elapsedRealtime() - startTimeMillis
                pendingFrameId++
                pendingFrameData = ByteBuffer.wrap(data)
                if (!pendingFrameData!!.hasArray() || pendingFrameData!!.array() != data) {
                    throw IllegalStateException("failed to create valid buffer for camera")
                }

                // notify processor thread if it is waiting on the next frame
                processorLock.notifyAll()
            }
        }

        override fun run() {
            while (true) {
                synchronized(processorLock) {
                    while (activate && (pendingFrameData == null)) {
                        try {
                            processorLock.wait()
                        } catch (e: InterruptedException) {
                            Log.d(TAG, "Frame processor worker thread is interrupted")
                            return
                        }
                    }

                    if (!activate) {
                        Log.d(TAG, "Frame processor worker thread shutdown")
                        return
                    }

                    try {
                        val bitmap = rsNV21ToRGB(pendingFrameData!!.array(), previewSize.width, previewSize.height)

                        callback.onReceiveFrameBitmap(bitmap, pendingFrameId)
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception thrown from detector receiver", e)
                    } finally {
                        camera.addCallbackBuffer(pendingFrameData!!.array())
                        pendingFrameData = null
                    }
                }


            }
        }
    }

    private fun rsNV21ToRGB(nv21: ByteArray, width:Int, height:Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val rs = RenderScript.create(context)
        val yuvToRgbSc = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))

        val yuvType = Type.Builder(rs, Element.U8(rs)).setX(nv21.size)
        val _in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT)

        val rgbaType = Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height)
        val _out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT)

        _in.copyFrom(nv21)

        yuvToRgbSc.setInput(_in)
        yuvToRgbSc.forEach(_out)

        _out.copyTo(bitmap)

        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}