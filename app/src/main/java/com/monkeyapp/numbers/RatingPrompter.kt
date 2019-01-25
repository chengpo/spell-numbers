/*
MIT License

Copyright (c) 2017 - 2019 Po Cheng

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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.SharedPreferences
import com.google.android.material.snackbar.Snackbar
import android.view.View
import androidx.core.content.edit
import com.monkeyapp.numbers.apphelpers.*

private const val PREF_NAME_RATE_APP = "SP_RATE_APP"
private const val PREF_KEY_IS_RATED_BOOLEAN = "SP_KEY_IS_RATED"
private const val PREF_KEY_LAST_PROMPT_TIME_LONG = "SP_KEY_LAST_PROMPT_TIME"

class RatingPrompter(private val anchorView: View) : LifecycleObserver {
    private val context: Context = anchorView.context.applicationContext

    private val ratePrefs: SharedPreferences
        get() = context.getSharedPreferences(PREF_NAME_RATE_APP, 0)

    private val isRated: Boolean
        get() = ratePrefs.getBoolean(PREF_KEY_IS_RATED_BOOLEAN, false)

    private val shouldPrompt: Boolean
        get() {
            val firstInstallTime = context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
            val lastPromptTime = ratePrefs.getLong(PREF_KEY_LAST_PROMPT_TIME_LONG, firstInstallTime)

            val timeout = (0.5 + Math.random() / 2) * 1000L * 60L * 60L
            return System.currentTimeMillis() > lastPromptTime + timeout
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!isRated && shouldPrompt) {
            promptRating()
        }
    }

    private fun promptRating() {
        anchorView.snackbar(R.string.rate_spell_numbers, Snackbar.LENGTH_INDEFINITE) {
            icon(R.drawable.ic_rate_app, R.color.accent)

            action(R.string.rate_sure, View.OnClickListener {
                context.browse(url = "market://details?id=com.monkeyapp.numbers",
                        newTask = true,
                        onError = {
                            context.browse(url = "https://play.google.com/store/apps/details?id=com.monkeyapp.numbers",
                                    newTask = true)
                        })

                ratePrefs.edit {
                    putBoolean(PREF_KEY_IS_RATED_BOOLEAN, true)
                }
            })

            dismissCallback {
                ratePrefs.edit {
                    putLong(PREF_KEY_LAST_PROMPT_TIME_LONG, System.currentTimeMillis())
                }
            }
        }
    }
}
