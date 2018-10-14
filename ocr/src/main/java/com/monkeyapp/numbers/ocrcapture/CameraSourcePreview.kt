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

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.common.images.Size
import com.monkeyapp.numbers.helpers.isPortraitMode
import java.io.IOException

private const val TAG = "CameraSourcePreview"
private const val DEFAULT_VIEW_WIDTH = 320
private const val DEFAULT_VIEW_HEIGHT = 240
@JvmField val DEFAULT_VIEW_SIZE = Size(DEFAULT_VIEW_WIDTH, DEFAULT_VIEW_HEIGHT)

class CameraSourcePreview : ViewGroup {
    private val surfaceView = SurfaceView(context)

    private var cameraSource: CameraSource? = null
    private var isSurfaceAvailable: Boolean = false
    private var isStartRequested: Boolean = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @SuppressLint("NewApi")
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

    fun setCameraSource(_cameraSource: CameraSource?) {
        if (_cameraSource == null) {
            return
        }

        cameraSource = _cameraSource
        isStartRequested = true
        startIfReady()
    }

    @SuppressLint("MissingPermission")
    fun startIfReady() {
        try {
            val permissions = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissions == PackageManager.PERMISSION_GRANTED) {
                if (isStartRequested and isSurfaceAvailable) {
                    cameraSource!!.start(surfaceView.holder)
                    isStartRequested = false
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to setCameraSource camera due to IOException")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to setCameraSource camera due to SecurityException")
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val size = cameraSource?.previewSize ?: DEFAULT_VIEW_SIZE

        var previewWidth = size.width
        var previewHeight = size.height

        if (isPortraitMode()) {
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
        val childXOffset: Int
        val childYOffset: Int

        if (widthRatio > heightRatio) {
            childWidth = viewWidth
            childHeight= (previewHeight * widthRatio).toInt()
            childXOffset = 0
            childYOffset = (childHeight - viewHeight) / 2
        } else {
            childWidth = (previewWidth * heightRatio).toInt()
            childHeight = viewHeight
            childXOffset = (childWidth - viewWidth) / 2
            childYOffset = 0
        }

        for (i in 0 until childCount) {
            getChildAt(i)
                    .layout(-1 * childXOffset,
                            -1 * childYOffset,
                            childWidth - childXOffset,
                            childHeight - childYOffset)
        }

        startIfReady()
    }
}