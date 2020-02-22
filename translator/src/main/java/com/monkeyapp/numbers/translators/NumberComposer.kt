/*
MIT License

Copyright (c) 2017 - 2020 Po Cheng

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

fun appendDigit(numberText: String, digit: Char): String {
    return when {
        numberText.contains('.') && digit == '.' -> numberText
        numberText.contains('.') && numberText.substringAfter(delimiter = ".", missingDelimiterValue = "").length >= 3 -> numberText
        numberText.isEmpty() && digit == '.' -> "0."
        numberText == "0" && digit == '.' -> "0."
        numberText == "0" && digit == '0' -> numberText
        numberText == "0" && digit != '0' -> "$digit"
        else -> numberText + digit
    }
}

fun deleteDigit(numberText: String): String {
    return when {
        numberText.isNotEmpty() -> numberText.substring(0, numberText.length - 1)
        else -> numberText
    }
}
