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

package com.monkeyapp.numbers.helper

import android.content.ActivityNotFoundException
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.content_number_word.*
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import com.monkeyapp.numbers.R

private const val SP_RATE_APP = "SP_RATE_APP"
private const val SP_KEY_IS_RATED = "SP_KEY_IS_RATED"
private const val SP_KEY_LAST_PROMPT_TIME = "SP_KEY_LAST_PROMPT_TIME"

fun AppCompatActivity.rateApp() {
    val pkgInfo = packageManager.getPackageInfo(packageName, 0)

    val lastPromptTime = getSharedPreferences(SP_RATE_APP, 0).getLong(SP_KEY_LAST_PROMPT_TIME, pkgInfo.lastUpdateTime)

    val shouldPrompt = (System.currentTimeMillis() - lastPromptTime) > 2 * 24 * 60 * 60 * 1000L
    val isRated = getSharedPreferences(SP_RATE_APP, 0).getBoolean(SP_KEY_IS_RATED, false)

    if (shouldPrompt && !isRated) {
        Snackbar.make(wordTextView, R.string.rate_spell_numbers, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.rate_sure, {
                    try {
                        startActivity(
                                Intent(Intent.ACTION_VIEW,
                                       Uri.parse("market://details?id=com.monkeyapp.numbers")))
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                                Intent(Intent.ACTION_VIEW,
                                       Uri.parse("https://play.google.com/store/apps/details?id=com.monkeyapp.numbers")))
                    }

                    getSharedPreferences(SP_RATE_APP, 0)
                            .edit()
                            .putBoolean(SP_KEY_IS_RATED, true)
                            .apply()
                })
                .addCallback(object :Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        getSharedPreferences(SP_RATE_APP, 0)
                                .edit()
                                .putLong(SP_KEY_LAST_PROMPT_TIME, System.currentTimeMillis())
                                .apply()
                    }
                })
                .setIcon(R.drawable.ic_rate_app, R.color.accent)
                .show()
    }
}