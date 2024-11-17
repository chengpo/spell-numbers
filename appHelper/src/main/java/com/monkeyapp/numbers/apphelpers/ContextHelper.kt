/*
MIT License

Copyright (c) 2017 - 2024 Po Cheng

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

package com.monkeyapp.numbers.apphelpers

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import java.lang.Exception

private const val INTENT_ACTION_OCR_CAPTURE = "com.monkeyapp.numbers.intent.OCR_CAPTURE"

inline val Context.isOcrAvailable:Boolean
    get() {
        return applicationContext
                .packageManager
                .getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                .metaData
                .getBoolean("is_ocr_supported")
    }

val Context.ocrIntent
    get()= Intent(INTENT_ACTION_OCR_CAPTURE).apply { `package` = packageName }

inline fun Context.browse(url: String, newTask: Boolean, onError: (e: Exception) -> Unit = {}) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            if (newTask) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        })
    } catch (e: ActivityNotFoundException) {
        onError(e)
    }
}

fun Context.getVectorDrawable(@DrawableRes drawableId: Int) = VectorDrawableCompat.create(resources, drawableId, theme)

fun Context.getCompatColor(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)