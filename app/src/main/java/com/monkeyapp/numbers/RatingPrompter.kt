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

package com.monkeyapp.numbers

import android.app.Activity
import android.content.SharedPreferences
import android.net.Uri
import com.google.android.material.snackbar.Snackbar
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.edit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.monkeyapp.numbers.apphelpers.*
import kotlin.math.absoluteValue

class RatingPrompter(private val activity: Activity,
                     private val anchorView: View) : DefaultLifecycleObserver, Snackbar.Callback() {
    private var snackbar: Snackbar? = null

    private inline val ratePrefs: SharedPreferences
        get() = activity.getSharedPreferences(PREF_NAME_RATE_APP, 0)

    private inline var isRated: Boolean
        get() = ratePrefs.getBoolean(PREF_KEY_IS_RATED_BOOLEAN, false)

        set(value) {
            ratePrefs.edit {
                putBoolean(PREF_KEY_IS_RATED_BOOLEAN, value)
            }
        }

    private inline var lastPromptTime: Long
        get() {
            val firstInstallTime = activity.packageManager.getPackageInfo(activity.packageName, 0).firstInstallTime
            return ratePrefs.getLong(PREF_KEY_LAST_PROMPT_TIME_LONG, firstInstallTime)
        }

        set(value) {
            ratePrefs.edit {
                putLong(PREF_KEY_LAST_PROMPT_TIME_LONG, value)
            }
        }

    private inline val shouldPrompt: Boolean
        get() {
            val timeout = (0.5 + Math.random() / 2) * 1000L * 60L * 60L
            return (System.currentTimeMillis() - lastPromptTime).absoluteValue >= timeout
        }

    fun bind(lifecycleOwner: LifecycleOwner) = lifecycleOwner.lifecycle.addObserver(this)

    override fun onResume(owner: LifecycleOwner) {
        if (snackbar == null && shouldPrompt) {
            // launch in-app review flow
            val reviewManager = ReviewManagerFactory.create(activity)
            val request = reviewManager.requestReviewFlow()
            request.addOnSuccessListener { reviewInfo ->
                reviewManager.launchReviewFlow(activity, reviewInfo)
                    .addOnFailureListener {
                        Firebase.analytics.logEvent("LaunchReviewFlowFailed") {}
                        showSnackbar()
                    }
                    .addOnCompleteListener {
                        Firebase.analytics.logEvent("LaunchReviewFlowCompleted") {}
                        lastPromptTime = System.currentTimeMillis()
                    }
            }.addOnFailureListener {
                Firebase.analytics.logEvent("RequestReviewFlowFailed") {
                    param("error", it.message ?: "unknown error")
                }
                showSnackbar()
            }
        }
    }

    private fun showSnackbar() {
        if (!isRated) {
            Firebase.analytics.logEvent("ShowRatingPromptSnackbar") {}

            snackbar = anchorView.snackbar(R.string.rate_spell_numbers, Snackbar.LENGTH_INDEFINITE) {
                icon(R.drawable.ic_rate_app, R.color.accent)

                action(R.string.rate_sure) {
                    activity.browse(url = "market://details?id=com.monkeyapp.numbers",
                        newTask = true,
                        onError = {
                            CustomTabsIntent.Builder().build()
                                .launchUrl(activity,  Uri.parse("https://play.google.com/store/apps/details?id=com.monkeyapp.numbers"))
                        })

                    Firebase.analytics.logEvent("LaunchGooglePlay") {}
                    isRated = true
                }

                addCallback(this@RatingPrompter)
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        snackbar?.let {
            it.removeCallback(this)
            it.dismiss()
        }

        snackbar = null
    }

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        lastPromptTime = System.currentTimeMillis()
    }

    private companion object {
        const val PREF_NAME_RATE_APP = "SP_RATE_APP"
        const val PREF_KEY_IS_RATED_BOOLEAN = "SP_KEY_IS_RATED"
        const val PREF_KEY_LAST_PROMPT_TIME_LONG = "SP_KEY_LAST_PROMPT_TIME"
    }
}
