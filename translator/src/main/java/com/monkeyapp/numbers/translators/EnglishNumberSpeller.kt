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

import java.lang.StringBuilder


class EnglishNumberSpeller: NumberSpeller() {
    private val symbols = listOf(
           "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
           "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen",
           "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

    private val radixSymbols = listOf("Hundred", "Thousand", "Million", "Billion", "Trillion")

    override fun spellNumber(wholeNumber: Long, fraction: Float) =
            "${spellWholeNumber(wholeNumber)} and ${spellFraction(fraction)}"

    private fun spellFraction(fraction: Float) =
            String.format("%02d / 100", Math.round(fraction * 100))

    private fun spellWholeNumber(wholeNumber: Long): String {
        if (wholeNumber == 0L) {
            return symbols[0]
        }

        val words = StringBuilder()
        var number = wholeNumber
        var radix = 0

        // find the maximum radix
        while (number >= 1000) {
            number /= 1000

            if (++radix >= radixSymbols.size) {
                throw LargeNumberException()
            }
        }

        number = wholeNumber

        while(radix >= 0) {
            val base = (Math.pow(1000.0, radix.toDouble())).toLong()
            val hundred = number / base
            if (hundred > 0) {
                spellHundreds(hundred, words)
                if (radix > 0) {
                    words.append("${radixSymbols[radix]} ")
                }
            }

            number %= base
            radix--
        }

        return words.toString().trimEnd()
    }

    private fun spellHundreds(num: Long, words: StringBuilder) {
        val hundred = (num / 100).toInt()
        if (hundred > 0) {
            words.append("${symbols[hundred]} ${radixSymbols[0]} ")
        }

        var remainders = (num % 100).toInt()
        when (remainders) {
            in 1 until 20 -> words.append("${symbols[remainders]} ")
            in 20 until 100 -> {
                val index = 20 + remainders / 10 - 2
                words.append("${symbols[index]} ")

                remainders %= 10
                if (remainders > 0) {
                    words.append("${symbols[remainders]} ")
                }
            }
        }
    }

}