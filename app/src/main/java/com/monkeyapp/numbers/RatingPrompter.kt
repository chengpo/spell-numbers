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

package com.monkeyapp.numbers

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.content.Context
import com.google.android.material.snackbar.Snackbar
import android.view.View
import androidx.core.content.edit
import com.monkeyapp.numbers.apphelpers.*

private const val PREF_NAME_RATE_APP = "SP_RATE_APP"
private const val PREF_KEY_IS_RATED_BOOLEAN = "SP_KEY_IS_RATED"
private const val PREF_KEY_LAST_PROMPT_TIME_LONG = "SP_KEY_LAST_PROMPT_TIME"

class RatingPrompter(private val context: Context, private val anchorView: View) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() = context.run {
        val pkgInfo = packageManager.getPackageInfo(packageName, 0)

        val ratePrefs = getSharedPreferences(PREF_NAME_RATE_APP, 0)
        val lastPromptTime = ratePrefs.getLong(PREF_KEY_LAST_PROMPT_TIME_LONG, pkgInfo.firstInstallTime)

        val isRated = ratePrefs.getBoolean(PREF_KEY_IS_RATED_BOOLEAN, false)
        val shouldPrompt = (!isRated) && System.currentTimeMillis() > lastPromptTime + (1 + 5 * Math.random()) * 60 * 60 * 1000L

        if (shouldPrompt) {
            // prompting user for rating App
            anchorView.snackbar(R.string.rate_spell_numbers, Snackbar.LENGTH_INDEFINITE) {
                icon(R.drawable.ic_rate_app, R.color.accent)

                action(R.string.rate_sure, View.OnClickListener { _ ->
                    browse(url = "market://details?id=com.monkeyapp.numbers",
                           newTask = true,
                           onError = {
                                browse(url = "https://play.google.com/store/apps/details?id=com.monkeyapp.numbers",
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
}
