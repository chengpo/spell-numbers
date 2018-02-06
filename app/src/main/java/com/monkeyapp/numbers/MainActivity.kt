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
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.content_number_word.*
import com.monkeyapp.numbers.translators.EnglishNumberSpeller.LargeNumberException
import com.monkeyapp.numbers.helpers.rateApp
import com.monkeyapp.numbers.helpers.setIcon
import com.monkeyapp.numbers.translators.RippleView

class MainActivity : AppCompatActivity() {
    companion object {
        private const val INTENT_ACTION_OCR_CAPTURE = "com.monkeyapp.numbers.intent.OCR_CAPTURE"
        private const val BUNDLE_EXTRA_DIGITS = "bundle_extra_digits"
        private const val RC_OCR_CAPTURE = 1000
    }

    private var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(myToolbar)

        omniButton.isCameraAvailable = applicationContext
                                            .packageManager
                                            .queryIntentActivities(
                                                    Intent(INTENT_ACTION_OCR_CAPTURE), 0)
                                            .isNotEmpty()

        omniButton.state = OmniButton.State.Camera

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel?.digitStr?.observe(this, object : Observer<String> {
            override fun onChanged(digits: String?) {
                digits?.let {
                    digitTextView.text = digits
                    omniButton.state = if (digits.isEmpty()) OmniButton.State.Camera
                                                        else OmniButton.State.Clean
                }
            }
        })

        mainViewModel?.numberStr?.observe(this, object : Observer<String> {
            override fun onChanged(numbers: String?) {
                wordTextView.text = numbers
            }
        })

        wordTextView.setOnClickListener{
            val numberWord = wordTextView.text.toString()
            if (numberWord.isNotBlank()) {
                rippleView.stopRippleAnimation(object : RippleView.onAnimationEndListener {
                    override fun onAnimationEnd() {
                        FullscreenActivity.start(this@MainActivity, numberWord)
                    }
                })
            }
        }

        wordTextView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                event?.let {
                    rippleView.startRippleAnimation(event.x, event.y)
                }

                return false
            }
        })


        rateApp()
    }
    
    fun onButtonClicked(button: View) {
        try {
            when (button.id) {
                R.id.btnDel -> mainViewModel?.deleteDigit()
                R.id.omniButton -> {
                    if (button is OmniButton) {
                        when (button.state) {
                            OmniButton.State.Clean -> mainViewModel?.resetDigit()
                            OmniButton.State.Camera -> {
                                val intent = Intent()
                                intent.action = INTENT_ACTION_OCR_CAPTURE
                                startActivityForResult(intent, RC_OCR_CAPTURE)
                            }
                        }
                    }

                }
                else -> if (button is Button)
                            when (button.text[0]) {
                                '.', in '0'..'9' -> mainViewModel?.appendDigit(button.text[0])
                            }
            }

        } catch (exception: LargeNumberException) {
            Snackbar.make(wordTextView, R.string.too_large_to_spell, Snackbar.LENGTH_LONG)
                    .setIcon(R.drawable.ic_error, R.color.accent)
                    .show()

            // revoke the last digit
            mainViewModel?.deleteDigit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putCharSequence(BUNDLE_EXTRA_DIGITS, digitTextView.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val digits: CharSequence? = savedInstanceState?.getCharSequence(BUNDLE_EXTRA_DIGITS)

        if (digits.toString() != mainViewModel?.digitStr?.value) {
            mainViewModel?.resetDigit()
            digits?.forEach { mainViewModel?.appendDigit(it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                R.id.action_about -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_OCR_CAPTURE && resultCode == Activity.RESULT_OK) {
            val number = data?.getStringExtra("number") ?: ""

            if (number.isNotBlank()) {
                mainViewModel?.resetDigit()
                number.forEach { mainViewModel?.appendDigit(it) }
            }
        }
    }
}
