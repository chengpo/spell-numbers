/*
MIT License

Copyright (c) 2017 - 2022 Po Cheng

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
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.monkeyapp.numbers.apphelpers.isPortraitMode

private const val CAPTURE_RECT_WIDTH_FACTOR = 0.7f
private const val CAPTURE_RECT_HEIGHT_FACTOR = 0.1f

class OcrOverlayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val viewRect = RectF()

    private val rectPaint by lazy {
        Paint().apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 6.0f
        }
    }

    private val shadowPaint by lazy {
        Paint().apply {
            color = ContextCompat.getColor(context, R.color.ocr_overlay_view_bg)
            style = Paint.Style.FILL
        }
    }

    private val ocrGraphicPaint by lazy {
        Paint().apply {
            color = ContextCompat.getColor(context, R.color.ocr_graphic_outline)
            style = Paint.Style.STROKE
            strokeWidth = 6.0f
        }
    }

    private var captureRect = RectF()
    var ocrGraphicList: List<OcrGraphic> = emptyList()
        set(ocrGraphics) {
            synchronized(this) {
                field = ocrGraphics
            }

            // run on main thread
            post {
                invalidate()
            }
        }

    fun calculateCaptureRect(viewWidth:Int, viewHeight:Int): RectF {
        val captureRectWidth = viewWidth * CAPTURE_RECT_WIDTH_FACTOR
        val captureRectHeight = viewHeight * CAPTURE_RECT_HEIGHT_FACTOR

        val rectLeft = (viewWidth - captureRectWidth) / 2F
        val rectTop = if (isPortraitMode)
                        (viewHeight/2 - captureRectHeight) / 2F
                      else
                        (viewHeight - captureRectHeight) / 2F

        val rectRight = rectLeft + captureRectWidth
        val rectBottom = rectTop + captureRectHeight

        return RectF(rectLeft, rectTop, rectRight, rectBottom)
    }

    fun offsetToCaptureRect(rect:RectF): RectF {
        rect.offset(captureRect.left, captureRect.top)
        return rect
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val viewWidth = right - left
        val viewHeight = bottom - top

        captureRect = calculateCaptureRect(viewWidth, viewHeight)
        viewRect.set(0F, 0F, viewWidth.toFloat(), viewHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.let { theCanvas ->
            // draw the capture rectangle
            theCanvas.drawRect(captureRect, rectPaint)

            // draw shadow background outsize of the capture rectangle
            theCanvas.save()
            theCanvas.clipRect(captureRect, Region.Op.DIFFERENCE)
            theCanvas.drawRect(viewRect, shadowPaint)
            theCanvas.restore()

            // draw captured text outlines
            var ocrGraphics = emptyList<OcrGraphic>()
            synchronized(this) {
                ocrGraphics = ocrGraphicList
            }

            ocrGraphics.forEach {
                it.draw(theCanvas, ocrGraphicPaint)
            }
        }
    }
}

class OcrGraphic(private val textRect: RectF) {
    fun draw(canvas: Canvas, paint: Paint) = canvas.drawRect(textRect, paint)
}
