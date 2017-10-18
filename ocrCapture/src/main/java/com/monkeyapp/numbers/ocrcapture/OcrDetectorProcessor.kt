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

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock


class OcrDetectorProcessor(val overlay: OcrGraphicOverlay): Detector.Processor<TextBlock> {
    private val TAG = "OcrDetectorProcessor"

    val rectPaint = Paint()
    val textPaint = Paint()

    init{
        rectPaint.color = Color.WHITE
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4.0f

        textPaint.color = Color.WHITE
        textPaint.textSize = 54.0f
    }

    override fun receiveDetections(detections: Detector.Detections<TextBlock>?) {
        Log.v(TAG, "receive ${detections?.detectedItems?.size()} text blocks")

        overlay.clear()

        val items = detections?.detectedItems
        val size = items?.size() ?: 0

        for (i in 0 until size) {
            val textBlock = items!!.get(i)
            if (textBlock != null) {
                val graphic = OcrGraphic(overlay, textBlock, rectPaint, textPaint)

                overlay.add(graphic)
            }
        }
    }

    override fun release() {
        overlay.clear()
    }
}