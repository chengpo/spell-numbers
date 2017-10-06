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
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import com.google.android.gms.vision.text.TextRecognizer
import android.util.Log
import android.widget.Toast
import android.content.Intent
import android.content.IntentFilter
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_ocr_capture.*
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.CommonStatusCodes
import java.io.IOException

class OcrCaptureActivity: AppCompatActivity() {
    private val TAG = "OcrCaptureActivity"
    private val RC_HANDLE_CAMERA_PERM = 2
    private val RC_HANDLE_GMS = 9001

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null
    private var cameraSource: CameraSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ocr_capture)

        val permissions = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissions == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {
            requestCameraPermission()
        }

        gestureDetector = GestureDetector(this, object: GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                return onTap(e?.rawX ?: 0f, e?.rawY ?: 0f) ||
                       super.onSingleTapConfirmed(e)
            }
        })

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
        startCameraSource()
    }

    override fun onPause() {
        super.onPause()

        preview.stop()
    }

    override fun onDestroy() {
        super.onDestroy()

        preview.release()
    }

    private fun onTap(rawX:Float, rawY:Float): Boolean {
        val graphc = graphicOverlay.getGraphic(rawX, rawY)
        val text = graphc?.textBlock
        if (text?.value != null ) {
            val data = Intent()
            data.putExtra("text", text.value)
            setResult(CommonStatusCodes.SUCCESS, data)
            finish()
            return true
        }

        return false
    }

    private fun requestCameraPermission() {
        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
            return
        }

        // show dialog for requesting camera permission rational
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return scaleGestureDetector!!.onTouchEvent(event) ||
               gestureDetector!!.onTouchEvent(event) ||
               super.onTouchEvent(event)
    }

    private fun createCameraSource() {
        val textRecognizer = TextRecognizer.Builder(this).build()
        textRecognizer.setProcessor(OcrDetectorProcessor(graphicOverlay))

        if (!textRecognizer.isOperational) {
            Log.w(TAG,  "Detector dependencies are not yet available.")

            val lowstorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowstorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show()
                Log.w(TAG, getString(R.string.low_storage_error))
            }

        }

        /*cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(400, 320)
                .setRequestedFps(15.0f)
                .setAutoFocusEnabled(true)
                .build()*/

        cameraSource = CameraSource(this, textRecognizer)
    }

    private fun startCameraSource() {
        // Check that the device has play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
            dlg.show()
        }

        try {
            if (cameraSource != null) {
                preview.start(cameraSource!!, graphicOverlay)
            }

        } catch (e : IOException) {
            Log.e(TAG, "Get IOException when start camera source")
            cameraSource?.stop()
        } catch (e : SecurityException) {
            Log.e(TAG, "Get SecurityException when start camera source")
            cameraSource?.stop()
        }
    }
}