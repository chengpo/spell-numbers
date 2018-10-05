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

class NumberComposer {
    private var integerDigits = mutableListOf<Char>()
    private var decimalDigits = mutableListOf<Char>()
    private var hasDecimal: Boolean = false

    val digitStr: String
        get() {
            if (hasDecimal) {
                if (integerDigits.isEmpty() && decimalDigits.isEmpty()) {
                    return "0"
                }

                if (integerDigits.isEmpty()) {
                    return "0.$decimalStr"
                }

                return "$integerStr.$decimalStr"
            }

            return integerStr
        }

    private val decimalStr: String
        get() = decimalDigits.joinToString(separator = "")

    private val integerStr: String
        get() {
            val integerWithComma = mutableListOf<Char>()

            integerDigits.reversed().forEachIndexed { index, digit ->
                if (index > 0 && index % 3 == 0) {
                    integerWithComma.add(',')
                }

                integerWithComma.add(digit)
            }

            return integerWithComma.reversed().joinToString(separator = "")
        }

    val integers: Long
        get() {
            var value = 0L

            integerDigits.forEach { digit ->
                value = (value * 10) + (digit - '0')
            }

            return value
        }

    val decimals: Float
        get() {
            var value = 0.0F

            decimalDigits.forEachIndexed { index, digit ->
                value += (digit - '0') * Math.pow(0.1, (index + 1).toDouble()).toFloat()
            }

            return value
        }

    fun appendDigit(digit: Char): Boolean {
        when (digit) {
            '.' -> {
                if (hasDecimal) {
                    return false
                }

                hasDecimal = true
            }
            in '0'..'9' -> {
                if (hasDecimal) {
                    if (decimalDigits.size < 3) {
                        decimalDigits.add(digit)
                    }
                } else {
                    if (integerDigits.size == 1 && integerDigits[0] == '0') {
                        return false
                    }

                    integerDigits.add(digit)
                }
            }
        }

        return true
    }

    fun resetDigit() {
        hasDecimal = false
        integerDigits.clear()
        decimalDigits.clear()
    }

    fun deleteDigit(): Boolean {
        if (hasDecimal) {
            if (decimalDigits.isEmpty()) {
                hasDecimal = false
                return true
            }

            decimalDigits.removeAt(decimalDigits.size - 1)
            if (decimalDigits.isEmpty()) {
                if (integerDigits.size == 1 && integerDigits[0] == '0') {
                    integerDigits.clear()
                }

                hasDecimal = false
            }

            return true
        }

        if (integerDigits.isEmpty()) {
            return false
        }

        integerDigits.removeAt(integerDigits.size - 1)
        return true
    }
}