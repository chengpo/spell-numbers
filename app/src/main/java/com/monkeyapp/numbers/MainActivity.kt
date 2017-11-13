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

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.content_number_word.*
import com.monkeyapp.numbers.NumberSpeller.LargeNumberException

class MainActivity : AppCompatActivity() {
    private val INTENT_ACTION_OCR_CAPTURE = "com.monkeyapp.numbers.intent.OCR_CAPTURE"
    private val BUNDLE_EXTRA_DIGITS = "bundle_extra_digits"
    private val RC_OCR_CAPTURE = 1000

    private val composer = NumberComposer()
    private val speller = NumberSpeller()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        digitTextButton.isCameraAvailable = packageManager
                                            .queryIntentActivities(
                                                    Intent(INTENT_ACTION_OCR_CAPTURE), 0)
                                            .isNotEmpty()

        digitTextButton.state = DigitTextViewUtilButton.STATE_CAMERA

        rateApp()
    }
    
    fun onDigitClicked(button: View) {
        try {
            when (button.id) {
                R.id.btnDel -> composer.deleteDigit()
                R.id.digitTextButton -> {
                    if (button is DigitTextViewUtilButton) {
                        when (button.state) {
                            DigitTextViewUtilButton.STATE_CLEAN -> composer.cleanDigit()
                            DigitTextViewUtilButton.STATE_CAMERA -> {
                                val intent = Intent()
                                intent.action = INTENT_ACTION_OCR_CAPTURE
                                startActivityForResult(intent, RC_OCR_CAPTURE)
                            }
                        }
                    }

                }
                else -> if (button is Button)
                            when (button.text[0]) {
                                '.', in '0'..'9' -> composer.appendDigit(button.text[0])
                            }
            }

            refreshDigitWords()
        } catch (exception: LargeNumberException) {
            Snackbar.make(wordTextView, R.string.too_large_to_spell, Snackbar.LENGTH_LONG)
                    .setIcon(R.drawable.ic_error, R.color.accent)
                    .show()

            // revoke the last digit
            composer.deleteDigit()
            refreshDigitWords()
        }
    }

    private fun refreshDigitWords() {
        digitTextView.text = composer.digitStr

        if (digitTextView.text.isNullOrEmpty()) {
            wordTextView.text = ""
            digitTextButton.state = DigitTextViewUtilButton.STATE_CAMERA
        } else {
            wordTextView.text = spellNumbers()
            digitTextButton.state = DigitTextViewUtilButton.STATE_CLEAN
        }
    }

    private fun spellNumbers(): String {
        val integerWords = speller.spellInteger(composer.integers)
                .flatMap { listOf(getString(it)) }
                .joinToString(separator = " ")

        val fractionWords = speller.spellFractions(composer.fractions)

        return "$integerWords and $fractionWords"
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putCharSequence(BUNDLE_EXTRA_DIGITS, digitTextView.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val digits: CharSequence? = savedInstanceState?.getCharSequence(BUNDLE_EXTRA_DIGITS)

        digits?.forEach { composer.appendDigit(it) }

        refreshDigitWords()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_OCR_CAPTURE && resultCode == Activity.RESULT_OK) {
            val number = data?.getStringExtra("number") ?: ""

            if (number.isNotBlank()) {
                composer.cleanDigit()
                number.forEach { composer.appendDigit(it) }
                refreshDigitWords()
            }

        }
    }
}
