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

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.google.android.gms.vision.text.TextBlock


class OcrGraphic(val overlay: OcrOverlayView,
                 val textBlock: TextBlock,
                 val rectPaint: Paint,
                 val textPaint: Paint) {
    var id = 0

    init {
        overlay.postInvalidate()
    }

    fun scaleX(horizontal: Float): Float {
        return horizontal * (overlay.widthScaleFactor)
    }

    fun scaleY(vertical: Float): Float {
        return vertical * (overlay.heightScaleFactor)
    }

    fun translateX(x: Float): Float {
        return scaleX(x)
    }

    fun translateY(y: Float): Float {
        return scaleY(y)
    }


    fun contains(x: Float, y: Float): Boolean {
        val rect = RectF(textBlock.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)

        return rect.contains(x, y)
    }

    fun draw(canvas: Canvas?) {
        val rect = RectF(textBlock.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)

        canvas!!.drawRect(rect, rectPaint)

        for (text in textBlock.components) {
            canvas.drawText(
                    text.value,
                    translateX(text.boundingBox.left.toFloat()),
                    translateY(text.boundingBox.bottom.toFloat()),
                    textPaint)
        }
    }

}