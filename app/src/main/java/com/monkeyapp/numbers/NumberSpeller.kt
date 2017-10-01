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

class NumberSpeller {
    class LargeNumberException : IllegalArgumentException("Number is too large to spellInteger")

    private val NUM_WORDS: IntArray = intArrayOf(
            R.string.num_zero,
            R.string.num_one,
            R.string.num_two,
            R.string.num_three,
            R.string.num_four,
            R.string.num_five,
            R.string.num_six,
            R.string.num_seven,
            R.string.num_eight,
            R.string.num_nine,
            R.string.num_ten,
            R.string.num_eleven,
            R.string.num_twelve,
            R.string.num_thirteen,
            R.string.num_fourteen,
            R.string.num_fifteen,
            R.string.num_sixteen,
            R.string.num_seventeen,
            R.string.num_eighteen,
            R.string.num_nineteen,
            R.string.num_twenty,
            R.string.num_thirty,
            R.string.num_forty,
            R.string.num_fifty,
            R.string.num_sixty,
            R.string.num_seventy,
            R.string.num_eighty,
            R.string.num_ninety)

    fun spellDecimal(decimals: Float): String {
        val cents = Math.round(decimals * 100)
        return  String.format("%02d / 100", cents)
    }

    fun spellInteger(integer: Long): List<Int> {
        when (integer) {
            in 0..19 -> return arrayListOf(NUM_WORDS[integer.toInt()])
            in 20..99 -> {
                val step = integer / 10 - 2
                return arrayListOf(NUM_WORDS[20 + step.toInt()]).plus(
                        if (integer % 10 > 0) spellInteger(integer % 10) else emptyList())
            }
            in 100..999 -> {
                val step = integer / 100
                return arrayListOf(NUM_WORDS[step.toInt()], R.string.num_hundred).plus(
                        if (integer % 100 > 0) spellInteger(integer % 100) else emptyList())
            }
            in 1000..1000 * 1000 - 1 -> {
                return spellInteger(integer / 1000).plus(arrayListOf(R.string.num_thousand)).plus(
                        if (integer % 1000 > 0) spellInteger(integer % 1000) else emptyList())
            }
            in 1000 * 1000..1000 * 1000 * 1000 - 1 -> {
                return spellInteger(integer / (1000 * 1000)).plus(arrayListOf(R.string.num_million)).plus(
                        if (integer % (1000 * 1000) > 0) spellInteger(integer % (1000 * 1000)) else emptyList())
            }
            in 1000 * 1000 * 1000..1000 * 1000 * 1000 * 1000L - 1 -> {
                return spellInteger(integer / (1000 * 1000 * 1000)).plus(arrayListOf(R.string.num_billion)).plus(
                        if (integer % (1000 * 1000 * 1000) > 0) spellInteger(integer % (1000 * 1000 * 1000)) else emptyList())
            }
            else -> throw LargeNumberException()
        }
    }
}