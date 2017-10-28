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

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.content_number_word.*
import com.monkeyapp.numbers.NumberSpeller.LargeNumberException

class MainActivity : AppCompatActivity() {
    private val INTENT_ACTION_OCR_CAPTURE = "com.monkeyapp.numbers.intent.OCR_CAPTURE"
    private val BUNDLE_EXTRA_DIGITS = "bundle_extra_digits"
    private val RC_OCR_CAPTURE = 1000

    private val composer = NumberComposer()
    private val speller = NumberSpeller()

    private val isCameraAvailable: Boolean
        get() {
            return  packageManager.queryIntentActivities(
                    Intent(INTENT_ACTION_OCR_CAPTURE), 0).isNotEmpty()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        digitTextButton.setCameraState()
    }
    
    fun onDigitClicked(button: View) {
        try {
            when (button.id) {
                R.id.btnDel -> composer.deleteDigit()
                R.id.digitTextButton -> {
                    if (button is DigitTextButton) {
                        button.onClicked()
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
                    .setIcon(R.drawable.ic_error, android.R.color.holo_orange_light)
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
            digitTextButton.setCameraState()
        } else {
            wordTextView.text = speller.spell(composer.integers, composer.fractions)
            digitTextButton.setCleanState()
        }
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

    private fun NumberSpeller.spell(integers : Long, decimals: Float): String {
        val integerWords = spellInteger(integers)
                .flatMap { listOf(getString(it)) }
                .joinToString(separator = " ")

        val decimalWords = spellDecimal(decimals)

        return "$integerWords and $decimalWords"
    }

    private fun Snackbar.setIcon(drawbleId: Int, tintColorId: Int): Snackbar {
        var errorIcon = VectorDrawableCompat.create(resources, drawbleId, theme) as Drawable
        val errorColor = ContextCompat.getColor(context, tintColorId)

        errorIcon = errorIcon.mutate()
        errorIcon = DrawableCompat.wrap(errorIcon)

        DrawableCompat.setTint(errorIcon, errorColor)
        DrawableCompat.setTintMode(errorIcon, PorterDuff.Mode.SRC_IN)

        val snackText : TextView = view.findViewById(android.support.design.R.id.snackbar_text)

        view.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_dark))
        snackText.setCompoundDrawablesWithIntrinsicBounds(errorIcon, null, null, null)
        snackText.compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)

        return this
    }

    private fun DigitTextButton.onClicked() {
        when (state) {
            DigitTextButton.STATE_CLEAN -> composer.cleanDigit()
            DigitTextButton.STATE_CAMERA -> {
                val intent = Intent()
                intent.action = INTENT_ACTION_OCR_CAPTURE
                startActivityForResult(intent, RC_OCR_CAPTURE)
            }
        }
    }

    private fun DigitTextButton.setCameraState() {
        if (isCameraAvailable) {
            digitTextButton.state = DigitTextButton.STATE_CAMERA
        } else {
            digitTextButton.state = DigitTextButton.STATE_CLEAN
            digitTextButton.visibility = View.INVISIBLE
        }
    }

    private fun DigitTextButton.setCleanState() {
        digitTextButton.state = DigitTextButton.STATE_CLEAN
        digitTextView.visibility = View.VISIBLE
    }
}
