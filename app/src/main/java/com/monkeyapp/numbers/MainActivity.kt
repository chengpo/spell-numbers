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
import com.monkeyapp.numbers.translators.EnglishNumberSpeller.LargeNumberException
import com.monkeyapp.numbers.helpers.rateApp
import com.monkeyapp.numbers.helpers.setIcon
import com.monkeyapp.numbers.translators.NumberObserver
import com.monkeyapp.numbers.translators.TranslatorFactory

class MainActivity : NumberObserver, AppCompatActivity() {
    companion object {
        private const val INTENT_ACTION_OCR_CAPTURE = "com.monkeyapp.numbers.intent.OCR_CAPTURE"
        private const val BUNDLE_EXTRA_DIGITS = "bundle_extra_digits"
        private const val RC_OCR_CAPTURE = 1000
    }

    private val translator = TranslatorFactory()
                                .getEnglishTranslator(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar = findViewById(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        omniButton.isCameraAvailable = packageManager
                                            .queryIntentActivities(
                                                    Intent(INTENT_ACTION_OCR_CAPTURE), 0)
                                            .isNotEmpty()

        omniButton.state = OmniButton.State.Camera

        rateApp()
    }
    
    fun onButtonClicked(button: View) {
        try {
            when (button.id) {
                R.id.btnDel -> translator.deleteDigit()
                R.id.omniButton -> {
                    if (button is OmniButton) {
                        when (button.state) {
                            OmniButton.State.Clean -> translator.resetDigit()
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
                                '.', in '0'..'9' -> translator.appendDigit(button.text[0])
                            }
            }

        } catch (exception: LargeNumberException) {
            Snackbar.make(wordTextView, R.string.too_large_to_spell, Snackbar.LENGTH_LONG)
                    .setIcon(R.drawable.ic_error, R.color.accent)
                    .show()

            // revoke the last digit
            translator.deleteDigit()
        }
    }

    override fun onNumberUpdated(digitStr:String, numberStr: String) {
        digitTextView.text = digitStr
        wordTextView.text = numberStr

        omniButton.state = if (digitStr.isEmpty())
                                OmniButton.State.Camera
                           else OmniButton.State.Clean
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putCharSequence(BUNDLE_EXTRA_DIGITS, digitTextView.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val digits: CharSequence? = savedInstanceState?.getCharSequence(BUNDLE_EXTRA_DIGITS)

        digits?.forEach { translator.appendDigit(it) }
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
                translator.resetDigit()
                number.forEach { translator.appendDigit(it) }
            }
        }
    }
}
