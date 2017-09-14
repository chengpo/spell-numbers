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

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import com.monkeyapp.numbers.NumberSpeller.LargeNumberException

class MainActivity : AppCompatActivity() {
    val composer = NumberComposer()
    val speller = NumberSpeller()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDigitClicked(digitButton: View) {
        if (digitButton is Button) {
            try {
                when (digitButton.text[0]) {
                    '.', in '0'..'9' -> {
                        composer.appendDigit(digitButton.text[0])
                    }
                    else -> {
                        composer.deleteDigit()
                    }
                }

                digitTextView.text = composer.number
                if (composer.number.isBlank()) {
                    wordTextView.text = ""
                } else {
                    wordTextView.text =
                            speller.spell(composer.integer)
                                    .flatMap { listOf(getString(it)) }
                                    .joinToString(separator = " ")
                }
            } catch (exception: LargeNumberException) {
                wordTextView.text = getString(R.string.too_large_to_spell)
            }
        }
    }
}
