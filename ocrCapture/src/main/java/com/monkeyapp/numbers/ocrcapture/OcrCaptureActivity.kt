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
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.ScaleGestureDetector
import com.google.android.gms.vision.text.TextRecognizer
import android.util.Log
import android.widget.Toast
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.RectF
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_ocr_capture.*
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import java.util.regex.Pattern

private const val RC_HANDLE_CAMERA_PERM = 200

class OcrCaptureActivity : AppCompatActivity() {
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var cameraSource: CameraSource? = null
    private var textRecognizer: Detector<TextBlock>? = null
    private var scaleHelper: ScaleHelper = ScaleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ocr_capture)

        val permissions = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissions == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {
            requestCameraPermission()
        }

        scaleGestureDetector = ScaleGestureDetector(this,
                object:ScaleGestureDetector.SimpleOnScaleGestureListener(){
                    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                        return true
                    }

                    override fun onScaleEnd(detector: ScaleGestureDetector?) {
                        // FIXME: scale to zoom
                    }

                    override fun onScale(detector: ScaleGestureDetector?): Boolean {
                        return false
                    }
                })
    }

    override fun onResume() {
        super.onResume()

        // Check that the device has play services available.
        val code = GoogleApiAvailability
                .getInstance()
                .isGooglePlayServicesAvailable(applicationContext)

        if (code != ConnectionResult.SUCCESS) {
             GoogleApiAvailability
                     .getInstance()
                     .getErrorDialog(this, code, /*RC_HANDLE_GMS*/9001)
                     .show()
        }

        cameraSourcePreview.setCameraSource(cameraSource)
    }

    override fun onPause() {
        super.onPause()

        cameraSource?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()

        cameraSource?.release()
        textRecognizer?.release()
    }

    private fun requestCameraPermission() =
        ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.CAMERA),
                                    RC_HANDLE_CAMERA_PERM)


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_HANDLE_CAMERA_PERM) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createCameraSource()
            } else {
                finish()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?) =
            scaleGestureDetector.onTouchEvent(event) || super.onTouchEvent(event)


    /**
     * Create camera source for OCR
     * @see <a href="https://codelabs.developers.google.com/codelabs/mobile-vision-ocr/">Google Play Mobile Vision OCR</a>
     */
    private fun createCameraSource() {
        textRecognizer = TextRecognizer.Builder(this).build()
        textRecognizer?.setProcessor(OcrDetectorProcessor())

        val isOperational = textRecognizer?.isOperational ?: false
        if (!isOperational) {
            Log.e("OcrCaptureActivity", "Text recognizer dependencies are not yet available.")

            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowStorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(this, R.string.ocr_low_storage_error, Toast.LENGTH_LONG).show()
                Log.e("OcrCaptureActivity", getString(R.string.ocr_low_storage_error))
            }
        }

        cameraSource = CameraSource(
                context = applicationContext,
                processFrameBitmap = { bitmap: Bitmap, frameId: Int ->
                    // crop frame bitmap from camera
                    val cropRect = ocrOverlayView.calculateCaptureRect(bitmap.width, bitmap.height)

                    val croppedBitmap = Bitmap.createBitmap(bitmap,
                            cropRect.left.toInt(), cropRect.top.toInt(),
                            cropRect.width().toInt(), cropRect.height().toInt(), null, true)

                    val outputFrame = Frame.Builder()
                            .setBitmap(croppedBitmap)
                            .setId(frameId)
                            .setRotation(0)
                            .build()

                    scaleHelper = ScaleHelper(bitmap.width, bitmap.height,
                            ocrOverlayView.width, ocrOverlayView.height)

                    textRecognizer?.receiveFrame(outputFrame)
                })
    }

    private class ScaleHelper(frameWidth:Int = 1, frameHeight:Int = 1,
                              viewWidth:Int = 1, viewHeight: Int = 1) {
        private val scaleX = viewWidth / frameWidth.toFloat()
        private val scaleY = viewHeight / frameHeight.toFloat()

        fun transX(x:Float) = x * scaleX
        fun transY(y:Float) = y * scaleY
    }

    inner class OcrDetectorProcessor : Detector.Processor<TextBlock> {
        private val numberPattern = Pattern.compile("(\\d+[\\s,.-]?\\d+)")
        private val textBlockMap = mutableMapOf<String, Int>()

        override fun release() {
            Log.v("OcrDetectorProcessor", "Released")
        }

        override fun receiveDetections(detections: Detector.Detections<TextBlock>?) {
            val items = detections?.detectedItems
            val size = items?.size() ?: 0
            val ocrGraphics = mutableListOf<OcrGraphic>()

            if (textBlockMap.size > 100) {
                textBlockMap.clear()
            }

            for (i in 0 until size) {
                val textBlock = items!!.get(i)
                if (textBlock != null) {
                    val matcher = numberPattern.matcher(textBlock.value)
                    while (matcher.find()) {
                        val number = matcher.group()
                        if (number.isNotBlank()) {
                            Log.v("OcrDetectorProcessor", "Detect number: ${matcher.group()}")

                            val count = textBlockMap[number]
                            val total = if (count != null) count + 1 else 1
                            textBlockMap.put(number, total)

                            var rect = RectF(textBlock.boundingBox)
                            rect.left = scaleHelper.transX(rect.left)
                            rect.top = scaleHelper.transY(rect.top)
                            rect.right = scaleHelper.transX(rect.right)
                            rect.bottom = scaleHelper.transX(rect.bottom)

                            rect = ocrOverlayView.offsetToCaptureRect(rect)

                            ocrGraphics.add(OcrGraphic(rect))

                            if (total >= 5) {
                                val data = Intent()
                                data.putExtra("number", number)
                                setResult(Activity.RESULT_OK, data)
                                finish()
                            }
                        }
                    }
                }
            }

            ocrOverlayView.ocrGraphicList = ocrGraphics
        }
    }
}