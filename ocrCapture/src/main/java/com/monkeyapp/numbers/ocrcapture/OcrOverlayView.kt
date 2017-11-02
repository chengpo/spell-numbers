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
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

class OcrOverlayView : View {
    val widthScaleFactor = 1.0f
    val heightScaleFactor = 1.0f

    companion object {
        val captureRectWidthFactor = 0.7f
        val captureRectHeightFactor = 0.1f
    }

    private val captureRect = RectF()
    private val viewRect = RectF()
    private val rectPaint = Paint()
    private val shadowPaint = Paint()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        rectPaint.color = Color.WHITE
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4.0f

        shadowPaint.color = ContextCompat.getColor(context, R.color.ocr_graphic_overlay_bg)
        shadowPaint.style = Paint.Style.FILL
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val viewWidth = (right - left).toFloat()
        val viewHeight = (bottom - top).toFloat()

        val captureRectWidth = viewWidth * captureRectWidthFactor
        val captureRectHeight = viewHeight * captureRectHeightFactor

        val rectLeft = (viewWidth - captureRectWidth) / 2F
        val rectTop = (viewHeight - captureRectHeight) / 2F
        val rectRight = rectLeft + captureRectWidth
        val rectBottom = rectTop + captureRectHeight

        captureRect.set(rectLeft, rectTop, rectRight, rectBottom)
        viewRect.set(0F, 0F, viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas!!)
        canvas.save()

        canvas.drawRect(captureRect, rectPaint)

        canvas.clipRect(captureRect, Region.Op.DIFFERENCE)
        canvas.drawRect(viewRect, shadowPaint)

        canvas.restore()
    }
}