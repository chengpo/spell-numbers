package com.monkeyapp.numbers.ocrcapture

import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock


class OcrDetectorProcessor(val overlay: OcrGraphicOverlay): Detector.Processor<TextBlock> {
    val rectPaint: Paint
    val textPaint: Paint

    init{
        rectPaint = Paint()
        rectPaint.color = Color.WHITE
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4.0f

        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 54.0f
    }


    override fun receiveDetections(detections: Detector.Detections<TextBlock>?) {
        overlay.clear()

        val items = detections?.detectedItems
        val size = items?.size() ?: 0
        for (i in 0 until size) {
            val textBlock = items!!.get(i)
            val graphic = OcrGraphic(overlay,
                                     textBlock,
                                     rectPaint,
                                     textPaint)

            overlay.add(graphic)
        }
    }

    override fun release() {
        overlay.clear()
    }
}