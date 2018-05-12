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

package com.monkeyapp.numbers.helpers

import android.content.ActivityNotFoundException
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.content_number_word.*
import android.support.v7.app.AppCompatActivity
import androidx.core.content.edit
import com.monkeyapp.numbers.R
import org.jetbrains.anko.browse

private const val SP_RATE_APP = "SP_RATE_APP"
private const val SP_KEY_IS_RATED = "SP_KEY_IS_RATED"
private const val SP_KEY_LAST_PROMPT_TIME = "SP_KEY_LAST_PROMPT_TIME"

fun AppCompatActivity.rateApp() {
    val pkgInfo = packageManager.getPackageInfo(packageName, 0)

    val ratePrefs = getSharedPreferences(SP_RATE_APP, 0)
    val lastPromptTime = ratePrefs.getLong(SP_KEY_LAST_PROMPT_TIME, pkgInfo.firstInstallTime)

    val shouldPrompt = (System.currentTimeMillis() - lastPromptTime) > 24 * 60 * 60 * 1000L
    val isRated = ratePrefs.getBoolean(SP_KEY_IS_RATED, false)

    if (shouldPrompt && !isRated) {
        wordTextView.snackbar(R.string.rate_spell_numbers, Snackbar.LENGTH_INDEFINITE)
        {
            icon(R.drawable.ic_rate_app, R.color.accent)

            action(R.string.rate_sure) {
                try {
                    browse(url = "market://details?id=com.monkeyapp.numbers",
                            newTask = true)
                } catch (e: ActivityNotFoundException) {
                    browse(url = "https://play.google.com/store/apps/details?id=com.monkeyapp.numbers",
                            newTask = true)
                }

                ratePrefs.edit {
                    putBoolean(SP_KEY_IS_RATED, true)
                }
            }

            dismissCallback {
                ratePrefs.edit {
                    putLong(SP_KEY_LAST_PROMPT_TIME, System.currentTimeMillis())
                }
            }
        }
    }
}