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
    var integerDigits = mutableListOf<Char>()
    var decimalDigits = mutableListOf<Char>()
    var isFictional : Boolean = false

    val number : String
        get() {
            if (isFictional) {
                if (decimalDigits.isEmpty()) {
                    return "0"
                }

                if (integerDigits.isEmpty()) {
                    return "0." +  decimalDigits.joinToString(separator = "")
                }

                return integerDigits.joinToString(separator = "") + "." + decimalDigits.joinToString(separator = "")
            } else {
                return integerDigits.joinToString(separator = "")
            }
        }

    val integer : Long
        get() {
            var _integer = 0L

            for (digit in integerDigits) {
                _integer = (_integer * 10) + (digit - '0')
            }

            return _integer
        }

    val decimal : Float
        get() {
            var _decimal = 0.0F
            var step = 0.1F

            for (digit in decimalDigits) {
                _decimal += (digit - '0') * step
                step *= 0.1F
            }

            return _decimal
        }

    fun appendDigit(digit : Char) : Boolean {
        when (digit) {
            '.' -> {
                if (isFictional) {
                   return false
                }

                isFictional = true
            }
            in '0'..'9' -> {
                if (isFictional) {
                    decimalDigits.add(digit)
                }
                else {
                    integerDigits.add(digit)
                }
            }
        }

        return true
    }

    fun deleteDigit()  {
        if (isFictional) {
            if (decimalDigits.isEmpty()) {
                isFictional = false
            } else {
                decimalDigits.removeAt(decimalDigits.size - 1)
                if (decimalDigits.isEmpty()) {
                    isFictional = false
                }
            }
        } else if (integerDigits.isNotEmpty()) {
                integerDigits.removeAt(integerDigits.size - 1)
        }
    }
}