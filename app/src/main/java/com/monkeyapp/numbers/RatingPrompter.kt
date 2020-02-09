/*
MIT License

Copyright (c) 2017 - 2020 Po Cheng

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
import androidx.lifecycle.LifecycleOwner
import com.monkeyapp.numbers.apphelpers.*
import kotlin.math.absoluteValue

class RatingPrompter(private val contextProvider: () -> Context,
                     private val anchorViewProvider: () -> View) : LifecycleObserver {
    private var snackbar: Snackbar? = null
    private var dismissCallback: Snackbar.Callback? = null

    private val ratePrefs: SharedPreferences
        get() = contextProvider().getSharedPreferences(PREF_NAME_RATE_APP, 0)

    private val isRated: Boolean
        get() = ratePrefs.getBoolean(PREF_KEY_IS_RATED_BOOLEAN, false)

    private val shouldPrompt: Boolean
        get() {
            val firstInstallTime = contextProvider().packageManager.getPackageInfo(contextProvider().packageName, 0).firstInstallTime
            val lastPromptTime = ratePrefs.getLong(PREF_KEY_LAST_PROMPT_TIME_LONG, firstInstallTime)

            val timeout = (0.5 + Math.random() / 2) * 1000L * 60L * 60L
            return (System.currentTimeMillis() - lastPromptTime).absoluteValue > timeout
        }

    fun attach(lifecycleOwner: LifecycleOwner) = lifecycleOwner.lifecycle.addObserver(this)

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!isRated && shouldPrompt) {
            promptRating()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        snackbar?.let {
            it.removeCallback(dismissCallback!!)
            it.dismiss()
        }

        snackbar = null
        dismissCallback = null
    }

    private fun promptRating() {
        snackbar = anchorViewProvider().snackbar(R.string.rate_spell_numbers, Snackbar.LENGTH_INDEFINITE) {
            icon(R.drawable.ic_rate_app, R.color.accent)

            action(R.string.rate_sure, View.OnClickListener {
                contextProvider().browse(url = "market://details?id=com.monkeyapp.numbers",
                        newTask = true,
                        onError = {
                            contextProvider().browse(url = "https://play.google.com/store/apps/details?id=com.monkeyapp.numbers",
                                    newTask = true)
                        })

                ratePrefs.edit {
                    putBoolean(PREF_KEY_IS_RATED_BOOLEAN, true)
                }
            })

            dismissCallback = object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    ratePrefs.edit {
                        putLong(PREF_KEY_LAST_PROMPT_TIME_LONG, System.currentTimeMillis())
                    }
                }
            }

            addCallback(dismissCallback!!)
        }
    }

    private companion object {
        const val PREF_NAME_RATE_APP = "SP_RATE_APP"
        const val PREF_KEY_IS_RATED_BOOLEAN = "SP_KEY_IS_RATED"
        const val PREF_KEY_LAST_PROMPT_TIME_LONG = "SP_KEY_LAST_PROMPT_TIME"
    }
}
