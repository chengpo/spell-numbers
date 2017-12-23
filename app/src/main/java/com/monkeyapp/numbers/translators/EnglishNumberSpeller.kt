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

package com.monkeyapp.numbers.translators

class EnglishNumberSpeller: NumberSpeller {
    companion object {
        @JvmField val NUM_WORDS = arrayOf(
                "Zero",
                "One",
                "Two",
                "Three",
                "Four",
                "Five",
                "Six",
                "Seven",
                "Eight",
                "Nine",
                "Ten",
                "Eleven",
                "Twelve",
                "Thirteen",
                "Fourteen",
                "Fifteen",
                "Sixteen",
                "Seventeen",
                "Eighteen",
                "Nineteen",
                "Twenty",
                "Thirty",
                "Forty",
                "Fifty",
                "Sixty",
                "Seventy",
                "Eighty",
                "Ninety")
    }

    class LargeNumberException : IllegalArgumentException("Number is too large to spellInteger")

    fun spellDecimals(decimals: Float) =
            String.format("%02d / 100", Math.round(decimals * 100))
    
    fun spellInteger(integer: Long): Array<String> =
            when (integer) {
                in 0..19 -> arrayOf(NUM_WORDS[integer.toInt()])
                in 20..99 -> {
                    val step = integer / 10 - 2
                    arrayOf(NUM_WORDS[20 + step.toInt()]).plus(
                            if (integer % 10 > 0) spellInteger(integer % 10) else emptyArray())
                }
                in 100..999 -> {
                    val step = integer / 100
                    arrayOf(NUM_WORDS[step.toInt()], "Hundred").plus(
                            if (integer % 100 > 0) spellInteger(integer % 100) else emptyArray())
                }
                in 1000 until 1000 * 1000 -> {
                    spellInteger(integer / 1000).plus(arrayOf("Thousand")).plus(
                            if (integer % 1000 > 0) spellInteger(integer % 1000) else emptyArray())
                }
                in 1000 * 1000 until 1000 * 1000 * 1000 -> {
                    spellInteger(integer / (1000 * 1000)).plus(arrayOf("Million")).plus(
                            if (integer % (1000 * 1000) > 0) spellInteger(integer % (1000 * 1000)) else emptyArray())
                }
                in 1000 * 1000 * 1000 until 1000 * 1000 * 1000 * 1000L -> {
                    spellInteger(integer / (1000 * 1000 * 1000)).plus(arrayOf("Billion")).plus(
                            if (integer % (1000 * 1000 * 1000) > 0) spellInteger(integer % (1000 * 1000 * 1000)) else emptyArray())
                }
                else -> throw LargeNumberException()
            }

    override fun spellNumber(integers: Long, decimals: Float) =
            "${spellInteger(integers).joinToString(separator = " ")} and ${spellDecimals(decimals)}"
}