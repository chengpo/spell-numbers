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

class NumberComposer {
    private var integerDigits = mutableListOf<Char>()
    private var fractionDigits = mutableListOf<Char>()
    private var isFictional: Boolean = false

    val digitStr: String
        get() {
            if (isFictional) {
                if (integerDigits.isEmpty() && fractionDigits.isEmpty()) {
                    return "0"
                }

                if (integerDigits.isEmpty()) {
                    return "0.$fractionStr"
                }

                return "$integerStr.$fractionStr"
            }

            return integerStr
        }

    private val fractionStr: String
        get() {
            return fractionDigits.joinToString(separator = "")
        }

    private val integerStr: String
        get() {
            val integerWithComma = mutableListOf<Char>()
            var i = 0

            integerDigits.reversed().forEach {
                if (i > 0 && i % 3 == 0) {
                    integerWithComma.add(',')
                }

                i++

                integerWithComma.add(it)
            }

            return integerWithComma.reversed().joinToString(separator = "")
        }

    val integers: Long
        get() {
            var _integers = 0L

            for (digit in integerDigits) {
                _integers = (_integers * 10) + (digit - '0')
            }

            return _integers
        }

    val fractions: Float
        get() {
            var _fractions = 0.0F
            var step = 0.1F

            for (digit in fractionDigits) {
                _fractions += (digit - '0') * step
                step *= 0.1F
            }

            return _fractions
        }

    fun appendDigit(digit: Char): Boolean {
        when (digit) {
            '.' -> {
                if (isFictional) {
                    return false
                }

                isFictional = true
            }
            in '0'..'9' -> {
                if (isFictional) {
                    if (fractionDigits.size < 3) {
                        fractionDigits.add(digit)
                    }
                } else {
                    integerDigits.add(digit)
                }
            }
        }

        return true
    }

    fun cleanDigit() {
        isFictional = false
        integerDigits.clear()
        fractionDigits.clear()
    }

    fun deleteDigit() {
        if (isFictional) {
            if (fractionDigits.isEmpty()) {
                isFictional = false
            } else {
                fractionDigits.removeAt(fractionDigits.size - 1)
                if (fractionDigits.isEmpty()) {
                    isFictional = false
                }
            }
        } else if (integerDigits.isNotEmpty()) {
            integerDigits.removeAt(integerDigits.size - 1)
        }
    }
}