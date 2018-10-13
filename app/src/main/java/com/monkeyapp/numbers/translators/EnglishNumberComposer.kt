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
    private var integerDigits = Stack<Char>()
    private var decimalDigits = Stack<Char>()
    private var hasDecimal: Boolean = false

    private val numberText: String
        get() {
            if (hasDecimal) {
                if (integerDigits.isEmpty() && decimalDigits.isEmpty()) {
                    return "0"
                }

                if (integerDigits.isEmpty()) {
                    return "0.$decimalText"
                }

                return "$integerText.$decimalText"
            }

            return integerText
        }

    private val decimalText: String
        get() = decimalDigits.joinToString(separator = "")

    private val integerText: String
        get() {
            val integerWithComma = Stack<Char>()

            integerDigits.reversed().forEachIndexed { index, digit ->
                if (index > 0 && index % 3 == 0) {
                    integerWithComma.push(',')
                }

                integerWithComma.push(digit)
            }

            return integerWithComma.reversed().joinToString(separator = "")
        }

    private val integers: Long
        get() {
            var value = 0L

            integerDigits.forEach { digit ->
                value = (value * 10) + (digit - '0')
            }

            return value
        }

    private val decimals: Float
        get() {
            var value = 0.0F

            decimalDigits.forEachIndexed { index, digit ->
                value += (digit - '0') * Math.pow(0.1, (index + 1).toDouble()).toFloat()
            }

            return value
        }

    override fun append(digit: Char) {
        when (digit) {
            '.' -> {
                if (!hasDecimal) {
                    hasDecimal = true
                }
            }
            in '0'..'9' -> {
                if (hasDecimal) {
                    if (decimalDigits.size < 3) {
                        decimalDigits.push(digit)
                    }
                } else {
                    if (integerDigits.size == 1 && integerDigits[0] == '0') {
                        // ignore duplicated 0
                    } else {
                        integerDigits.push(digit)
                    }
                }
            }
        }

        observerCallback(numberText, integers, decimals)
    }

    override fun reset() {
        hasDecimal = false
        integerDigits.clear()
        decimalDigits.clear()

        observerCallback(numberText, integers, decimals)
    }

    override fun backspace() {
        if (hasDecimal) {
            if (decimalDigits.isEmpty()) {
                hasDecimal = false
            } else {
                decimalDigits.pop()

                if (decimalDigits.isEmpty()) {
                    if (integerDigits.size == 1 && integerDigits[0] == '0') {
                        integerDigits.clear()
                    }

                    hasDecimal = false
                }
            }
        } else if (!integerDigits.isEmpty()) {
            integerDigits.pop()
        }

        observerCallback(numberText, integers, decimals)
    }

    override fun observe(callback: (numberText: String, integers: Long, decimals: Float) -> Unit) {
        observerCallback = callback
    }
}