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
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.vision.CameraSource


class OcrGraphicOverlay : View {
    var widthScaleFactor = 1.0f
    var heightScaleFactor = 1.0f

    var previewWidth = 0
    var previewHeight = 0
    var facing = CameraSource.CAMERA_FACING_BACK


    var graphicList = mutableListOf<OcrGraphic>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
        synchronized(this) {
            this.previewWidth = previewWidth
            this.previewHeight = previewHeight
            this.facing = facing
        }

        postInvalidate()
    }

    fun clear() {
        synchronized(this) {
            graphicList.clear()
        }

        postInvalidate()
    }

    fun add(graphic: OcrGraphic) {
        synchronized(this) {
            graphicList.add(graphic)
        }

        postInvalidate()
    }

    fun remove(graphic: OcrGraphic) {
        synchronized(this) {
            graphicList.remove(graphic)
        }

        postInvalidate()
    }

    fun getGraphic(x: Float, y: Float): OcrGraphic? {
        val location = IntArray(2)
        getLocationOnScreen(location)

        synchronized(this) {
            for (graphic in graphicList) {
                if (graphic.contains(x - location[0],
                                     y - location[1])) {
                    return graphic
                }
            }
        }

        return null
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        synchronized(this) {
            if (previewWidth != 0 && previewHeight != 0) {
                widthScaleFactor = canvas!!.getWidth().toFloat() / previewWidth.toFloat()
                heightScaleFactor = canvas.getHeight().toFloat() / previewHeight.toFloat()
            }

            for (graphic in graphicList) {
                graphic.draw(canvas)
            }
        }
    }
}