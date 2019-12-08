/*
MIT License

Copyright (c) 2017 - 2019 Po Cheng

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

import java.lang.StringBuilder

fun formatNumber(numberText: String, delimiter: Char, delimiterWidth: Int): String {
    val dotIndex = numberText.indexOf(".")

    val wholeNumberText = if (dotIndex < 0) numberText else numberText.substring(0, dotIndex)
    val fractionText = if (dotIndex < 0) null else numberText.substring(dotIndex)
    val sb = StringBuilder()

    // trim the leading '0's and separate digits with delimiter
    wholeNumberText
            .trimStart { it == '0' }
            .reversed()
            .forEachIndexed { index, digit ->
                if (index > 0 && index % delimiterWidth == 0) {
                    sb.append(delimiter)
                }
                sb.append(digit)
            }

    sb.reverse()

    // set whole number = 0 for empty whole number string
    if (sb.isEmpty()) {
        sb.append('0')
    }

    if (fractionText != null) {
        sb.append(fractionText)
    }

    return sb.toString()
}