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

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.v4.app.ActivityCompat
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.vision.CameraSource
import java.io.IOException

class CameraSourcePreview: ViewGroup {
    private val TAG = "CameraSourcePreview"
    private val DEFAULT_VIEW_WIDTH = 320
    private val DEFAULT_VIEW_HEIGHT = 240

    private val surfaceView = SurfaceView(context)

    private var cameraSource: CameraSource? = null
    private var isSurfaceAvailable: Boolean = false
    private var isStartRequested: Boolean = false

    private var overlay: OcrGraphicOverlay? = null

    private val isPortraitMode: Boolean
        get() {
            return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        surfaceView.holder.addCallback(object: SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                isSurfaceAvailable = true

                startIfReady()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                isSurfaceAvailable = false
            }

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }
        })

        addView(surfaceView)
    }

    fun start(cameraSource: CameraSource, overlay: OcrGraphicOverlay) {
        this.cameraSource = cameraSource
        this.overlay = overlay

        this.isStartRequested = true
        startIfReady()
    }

    fun stop() {
        cameraSource?.stop()
    }

    fun release() {
        cameraSource?.release()
        cameraSource = null
    }

    @SuppressLint("MissingPermission")
    fun startIfReady() {
        try {
            val permissions = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissions == PackageManager.PERMISSION_GRANTED) {

                if (isStartRequested and isSurfaceAvailable) {
                    cameraSource!!.start(surfaceView.holder)
                    val size = cameraSource!!.previewSize
                    val width = size.width
                    val height = size.height

                    val min = Math.min(width, height)
                    val max = Math.max(width, height)

                    if (isPortraitMode) {
                        overlay!!.setCameraInfo(min, max, cameraSource!!.cameraFacing)
                    } else {
                        overlay!!.setCameraInfo(max, min, cameraSource!!.cameraFacing)
                    }

                    overlay!!.clear()

                    isStartRequested = false
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to start camera due to IOException")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to start camera due to SecurityException")
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val size = cameraSource?.previewSize
        var previewWidth = size?.width ?: DEFAULT_VIEW_WIDTH
        var previewHeight = size?.height ?: DEFAULT_VIEW_HEIGHT

        if (isPortraitMode) {
            val tmp = previewWidth
            previewWidth = previewHeight
            previewHeight = tmp
        }

        val viewWidth = right - left
        val viewHeight = bottom - top

        val widthRatio = viewWidth.toFloat() / previewWidth.toFloat()
        val heightRatio = viewHeight.toFloat() / previewHeight.toFloat()

        val childWidth: Int
        val childHeight: Int
        var childXOffset = 0
        var childYOffset = 0

        if (widthRatio > heightRatio) {
            childWidth = viewWidth
            childHeight= (previewHeight * widthRatio).toInt()
            childYOffset = (childHeight - viewHeight) / 2
        } else {
            childWidth = (previewWidth * heightRatio).toInt()
            childHeight = viewHeight
            childXOffset = (childWidth - viewWidth) / 2
        }

        for (i in 0 until childCount) {
            getChildAt(i).layout(-1 * childXOffset, -1 * childYOffset,
                    childWidth - childXOffset, childHeight - childYOffset)
        }

        startIfReady()
    }
}