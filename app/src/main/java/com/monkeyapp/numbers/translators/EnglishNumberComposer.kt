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

package com.monkeyapp.numbers.translators

import java.util.*

class EnglishNumberComposer: NumberComposer.Observable {
    private var observerCallback: (numberText: String, integers: Long, decimals: Float) -> Unit = { _, _, _ -> }
    private var wholeNumberDigits = Stack<Char>()
    private var fractionDigits = Stack<Char>()
    private var isDecimal: Boolean = false

    private val numberText: String
        get() {
            if (isDecimal) {
                if (wholeNumberDigits.isEmpty() && fractionDigits.isEmpty()) {
                    return "0"
                }

                if (wholeNumberDigits.isEmpty()) {
                    return "0.$fractionText"
                }

                return "$wholeNumberText.$fractionText"
            }

            return wholeNumberText
        }

    private val fractionText: String
        get() = fractionDigits.joinToString(separator = "")

    private val wholeNumberText: String
        get() {
            val integerWithComma = Stack<Char>()

            wholeNumberDigits.reversed().forEachIndexed { index, digit ->
                if (index > 0 && index % 3 == 0) {
                    integerWithComma.push(',')
                }

                integerWithComma.push(digit)
            }

            return integerWithComma.reversed().joinToString(separator = "")
        }

    private val wholeNumber: Long
        get() {
            var value = 0L

            wholeNumberDigits.forEach { digit ->
                value = (value * 10) + (digit - '0')
            }

            return value
        }

    private val fraction: Float
        get() {
            var value = 0.0F

            fractionDigits.forEachIndexed { index, digit ->
                value += (digit - '0') * Math.pow(0.1, (index + 1).toDouble()).toFloat()
            }

            return value
        }

    override fun append(digit: Char) {
        when (digit) {
            '.' -> {
                if (!isDecimal) {
                    isDecimal = true
                }
            }
            in '0'..'9' -> {
                if (isDecimal) {
                    if (fractionDigits.size < 3) {
                        fractionDigits.push(digit)
                    }
                } else {
                    if (wholeNumberDigits.size == 1 && wholeNumberDigits[0] == '0') {
                        // ignore duplicated 0
                    } else {
                        wholeNumberDigits.push(digit)
                    }
                }
            }
        }

        observerCallback(numberText, wholeNumber, fraction)
    }

    override fun reset() {
        isDecimal = false
        wholeNumberDigits.clear()
        fractionDigits.clear()

        observerCallback(numberText, wholeNumber, fraction)
    }

    override fun backspace() {
        if (isDecimal) {
            if (fractionDigits.isEmpty()) {
                isDecimal = false
            } else {
                fractionDigits.pop()

                if (fractionDigits.isEmpty()) {
                    if (wholeNumberDigits.size == 1 && wholeNumberDigits[0] == '0') {
                        wholeNumberDigits.clear()
                    }

                    isDecimal = false
                }
            }
        } else if (!wholeNumberDigits.isEmpty()) {
            wholeNumberDigits.pop()
        }

        observerCallback(numberText, wholeNumber, fraction)
    }

    override fun observe(callback: (numberText: String, integers: Long, decimals: Float) -> Unit) {
        observerCallback = callback
    }
}