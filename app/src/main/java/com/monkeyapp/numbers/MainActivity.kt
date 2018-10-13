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

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import arrow.core.Try
import arrow.core.getOrElse

import com.monkeyapp.numbers.helpers.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_number_word.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        // bind lifecycle to rating helper
        lifecycle.addObserver(RatingPrompter(this, wordsTextView))

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.observe(this) { viewObj ->
            viewObj?.let {
                numberTextView.text = it.numberText
                omniButton.state = if (it.numberText.isEmpty())
                    OmniButton.State.Camera
                else
                    OmniButton.State.Clean

                wordsTextView.text = it.wordsText
            }
        }

        wordsTextView.setOnClickListener {
            val wordsText = wordsTextView.text.toString()
            if (wordsText.isNotBlank()) {
                rippleView.stopRippleAnimation {
                    FullScreenActivity.show(this, wordsText)
                }
            }
        }

        wordsTextView.setOnTouchListener { _, event ->
            rippleView.startRippleAnimation(event.x, event.y)
            false
        }
    }

    fun onButtonClicked(button: View) {
        when {
            button.id == R.id.btnDel ->
                mainViewModel.backspace()

            button is OmniButton ->
                when (button.state) {
                    OmniButton.State.Clean ->
                        mainViewModel.reset()
                    OmniButton.State.Camera ->
                        startActivityForResult(
                            Intent(INTENT_ACTION_OCR_CAPTURE),
                            RC_OCR_CAPTURE)
                }

            button is Button && (button.text[0] == '.' || button.text[0] in '0'..'9') ->
                Try {
                    mainViewModel.append(button.text[0])
                }.getOrElse {
                    wordsTextView.snackbar(R.string.too_large_to_spell) {
                        icon(R.drawable.ic_error, R.color.accent)
                    }

                    // revoke the last digit
                    mainViewModel.backspace()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
            when (item?.itemId) {
                R.id.action_about -> {
                    AboutActivity.show(this)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_OCR_CAPTURE && resultCode == Activity.RESULT_OK) {
            val number = data?.getStringExtra("number") ?: ""
            if (number.isNotBlank()) {
                Try {
                    mainViewModel.reset()
                    number.forEach { digit ->
                        mainViewModel.append(digit)
                    }
                }.getOrElse {
                    wordsTextView.snackbar(R.string.too_large_to_spell) {
                        icon(R.drawable.ic_error, R.color.accent)
                    }

                    mainViewModel.reset()
                }

            }
        }
    }
}
