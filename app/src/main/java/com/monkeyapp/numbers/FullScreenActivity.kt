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

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.NavUtils
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreenActivity : AppCompatActivity() {
    companion object {
        fun show(context: Context, wordsText: String) {
            val intent = Intent(context, FullScreenActivity::class.java)
            intent.putExtra("number_words", wordsText)
            context.startActivity(intent)
        }
    }

    private val hideHandler = Handler(Looper.getMainLooper())

    private val hideRunnable = Runnable {
        systemUiVisible = false
    }

    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        wordsTextView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }

    private var systemUiVisible: Boolean = true
        set(isVisible) {
            // Some older devices needs a small delay between UI widget updates
            // and a change of the status and navigation bar.
            val uiAutomationDelay = 300L

            fun show() {
                field = true

                // Show the system bar
                wordsTextView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                // Schedule a runnable to display UI elements after a delay
                hideHandler.removeCallbacks(hidePart2Runnable)
                hideHandler.postDelayed(showPart2Runnable, uiAutomationDelay)
            }

            fun hide() {
                field = false

                // Hide UI first
                supportActionBar?.hide()

                // Schedule a runnable to remove the status and navigation bar after a delay
                hideHandler.removeCallbacks(showPart2Runnable)
                hideHandler.postDelayed(hidePart2Runnable, uiAutomationDelay)
            }

            if (isVisible)
                show()
            else
                hide()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        wordsTextView.text = intent.getStringExtra("number_words")
        wordsTextView.setOnClickListener {
            // toggle system ui visibility
            systemUiVisible = !systemUiVisible
        }

        // Wait after user interaction before hiding the system UI.
        delayedHide(3000L)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100L)
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Long) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when(item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button.
                    NavUtils.navigateUpFromSameTask(this)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}