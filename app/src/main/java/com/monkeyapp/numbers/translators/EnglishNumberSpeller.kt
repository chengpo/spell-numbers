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

    private fun spellDecimals(decimals: Float) =
            String.format("%02d / 100", Math.round(decimals * 100))
    
    fun spellInteger(integer: Long): StringBuilder =
            when (integer) {
                in 0 until 20 -> StringBuilder().append(symbols[integer.toInt()])
                in 20 until 100 -> {
                    val index = integer / 10 - 2
                    StringBuilder()
                            .append(symbols[20 + index.toInt()])
                            .append(if (integer % 10 > 0) " ${spellInteger(integer % 10)}" else "")
                }
                in 100 until 1000 -> {
                    val index = integer / 100
                    StringBuilder()
                            .append("${symbols[index.toInt()]} Hundred")
                            .append(if (integer % 100 > 0) " ${spellInteger(integer % 100)}" else "")
                }
                in 1000 until 1000 * 1000 -> {
                    spellInteger(integer / 1000)
                            .append(" Thousand")
                            .append(if (integer % 1000 > 0) " ${spellInteger(integer % 1000)}" else "")
                }
                in 1000 * 1000 until 1000 * 1000 * 1000 -> {
                    spellInteger(integer / (1000 * 1000))
                            .append(" Million")
                            .append(if (integer % (1000 * 1000) > 0) " ${spellInteger(integer % (1000 * 1000))}" else "")
                }
                in 1000 * 1000 * 1000 until 1000 * 1000 * 1000 * 1000L -> {
                    spellInteger(integer / (1000 * 1000 * 1000))
                            .append(" Billion")
                            .append(if (integer % (1000 * 1000 * 1000) > 0) " ${spellInteger(integer % (1000 * 1000 * 1000))}" else "")
                }
                else -> throw LargeNumberException()
            }

    override fun spellNumber(integers: Long, decimals: Float) =
            "${spellInteger(integers)} and ${spellDecimals(decimals)}"
}