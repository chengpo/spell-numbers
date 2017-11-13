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

package com.monkeyapp.numbers

import android.content.ActivityNotFoundException
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.content_number_word.*
import android.content.Intent
import android.net.Uri


const val SP_RATE_APP = "SP_RATE_APP"
const val SP_KEY_IS_RATED = "SP_KEY_IS_RATED"

fun MainActivity.rateApp() {
    val pkgInfo = packageManager.getPackageInfo(packageName, 0)

    val installedMoreThanTwoDays = System.currentTimeMillis() - pkgInfo.firstInstallTime > 2 * 24 * 60 * 60 * 1000
    val isRated = getSharedPreferences(SP_RATE_APP, 0).getBoolean(SP_KEY_IS_RATED, false)

    if (installedMoreThanTwoDays && !isRated) {
        Snackbar.make(wordTextView, R.string.rate_spell_numbers, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.rate_sure, {
                    try {
                        startActivity(
                                Intent(Intent.ACTION_VIEW,
                                       Uri.parse("market://details?id=$packageName")))
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                                Intent(Intent.ACTION_VIEW,
                                       Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                    }

                    getSharedPreferences(SP_RATE_APP, 0).edit().putBoolean(SP_KEY_IS_RATED, true).apply()
                })
                .show()
    }
}