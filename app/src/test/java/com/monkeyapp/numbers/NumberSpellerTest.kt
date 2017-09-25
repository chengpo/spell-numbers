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

import org.junit.Test

import org.junit.Assert.*


class NumberSpellerTest {
    @Test
    fun testSpellTens() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 0", intArrayOf(R.string.num_zero), speller.spellInteger(0).toIntArray())
        assertArrayEquals("spellInteger digitStr 1", intArrayOf(R.string.num_one), speller.spellInteger(1).toIntArray())
        assertArrayEquals("spellInteger digitStr 2", intArrayOf(R.string.num_two), speller.spellInteger(2).toIntArray())
        assertArrayEquals("spellInteger digitStr 3", intArrayOf(R.string.num_three), speller.spellInteger(3).toIntArray())
        assertArrayEquals("spellInteger digitStr 4", intArrayOf(R.string.num_four), speller.spellInteger(4).toIntArray())
        assertArrayEquals("spellInteger digitStr 5", intArrayOf(R.string.num_five), speller.spellInteger(5).toIntArray())
        assertArrayEquals("spellInteger digitStr 6", intArrayOf(R.string.num_six), speller.spellInteger(6).toIntArray())
        assertArrayEquals("spellInteger digitStr 7", intArrayOf(R.string.num_seven), speller.spellInteger(7).toIntArray())
        assertArrayEquals("spellInteger digitStr 8", intArrayOf(R.string.num_eight), speller.spellInteger(8).toIntArray())
        assertArrayEquals("spellInteger digitStr 9", intArrayOf(R.string.num_nine), speller.spellInteger(9).toIntArray())
        assertArrayEquals("spellInteger digitStr 10", intArrayOf(R.string.num_ten), speller.spellInteger(10).toIntArray())
        assertArrayEquals("spellInteger digitStr 11", intArrayOf(R.string.num_eleven), speller.spellInteger(11).toIntArray())
        assertArrayEquals("spellInteger digitStr 12", intArrayOf(R.string.num_twelve), speller.spellInteger(12).toIntArray())
        assertArrayEquals("spellInteger digitStr 13", intArrayOf(R.string.num_thirteen), speller.spellInteger(13).toIntArray())
        assertArrayEquals("spellInteger digitStr 14", intArrayOf(R.string.num_fourteen), speller.spellInteger(14).toIntArray())
        assertArrayEquals("spellInteger digitStr 15", intArrayOf(R.string.num_fifteen), speller.spellInteger(15).toIntArray())
        assertArrayEquals("spellInteger digitStr 16", intArrayOf(R.string.num_sixteen), speller.spellInteger(16).toIntArray())
        assertArrayEquals("spellInteger digitStr 17", intArrayOf(R.string.num_seventeen), speller.spellInteger(17).toIntArray())
        assertArrayEquals("spellInteger digitStr 18", intArrayOf(R.string.num_eighteen), speller.spellInteger(18).toIntArray())
        assertArrayEquals("spellInteger digitStr 19", intArrayOf(R.string.num_nineteen), speller.spellInteger(19).toIntArray())
    }

    @Test
    fun testSpellTwenties() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 20", intArrayOf(R.string.num_twenty), speller.spellInteger(20).toIntArray())
        assertArrayEquals("spellInteger digitStr 21", intArrayOf(R.string.num_twenty, R.string.num_one), speller.spellInteger(21).toIntArray())
        assertArrayEquals("spellInteger digitStr 22", intArrayOf(R.string.num_twenty, R.string.num_two), speller.spellInteger(22).toIntArray())
        assertArrayEquals("spellInteger digitStr 23", intArrayOf(R.string.num_twenty, R.string.num_three), speller.spellInteger(23).toIntArray())
        assertArrayEquals("spellInteger digitStr 24", intArrayOf(R.string.num_twenty, R.string.num_four), speller.spellInteger(24).toIntArray())
        assertArrayEquals("spellInteger digitStr 25", intArrayOf(R.string.num_twenty, R.string.num_five), speller.spellInteger(25).toIntArray())
        assertArrayEquals("spellInteger digitStr 26", intArrayOf(R.string.num_twenty, R.string.num_six), speller.spellInteger(26).toIntArray())
        assertArrayEquals("spellInteger digitStr 27", intArrayOf(R.string.num_twenty, R.string.num_seven), speller.spellInteger(27).toIntArray())
        assertArrayEquals("spellInteger digitStr 28", intArrayOf(R.string.num_twenty, R.string.num_eight), speller.spellInteger(28).toIntArray())
        assertArrayEquals("spellInteger digitStr 29", intArrayOf(R.string.num_twenty, R.string.num_nine), speller.spellInteger(29).toIntArray())
    }

    @Test
    fun testSpellThrties() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 30", intArrayOf(R.string.num_thirty), speller.spellInteger(30).toIntArray())
        assertArrayEquals("spellInteger digitStr 31", intArrayOf(R.string.num_thirty, R.string.num_one), speller.spellInteger(31).toIntArray())
        assertArrayEquals("spellInteger digitStr 32", intArrayOf(R.string.num_thirty, R.string.num_two), speller.spellInteger(32).toIntArray())
        assertArrayEquals("spellInteger digitStr 33", intArrayOf(R.string.num_thirty, R.string.num_three), speller.spellInteger(33).toIntArray())
        assertArrayEquals("spellInteger digitStr 34", intArrayOf(R.string.num_thirty, R.string.num_four), speller.spellInteger(34).toIntArray())
        assertArrayEquals("spellInteger digitStr 35", intArrayOf(R.string.num_thirty, R.string.num_five), speller.spellInteger(35).toIntArray())
        assertArrayEquals("spellInteger digitStr 36", intArrayOf(R.string.num_thirty, R.string.num_six), speller.spellInteger(36).toIntArray())
        assertArrayEquals("spellInteger digitStr 37", intArrayOf(R.string.num_thirty, R.string.num_seven), speller.spellInteger(37).toIntArray())
        assertArrayEquals("spellInteger digitStr 38", intArrayOf(R.string.num_thirty, R.string.num_eight), speller.spellInteger(38).toIntArray())
        assertArrayEquals("spellInteger digitStr 39", intArrayOf(R.string.num_thirty, R.string.num_nine), speller.spellInteger(39).toIntArray())
    }

    @Test
    fun testSpellForties() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 40", intArrayOf(R.string.num_forty), speller.spellInteger(40).toIntArray())
        assertArrayEquals("spellInteger digitStr 41", intArrayOf(R.string.num_forty, R.string.num_one), speller.spellInteger(41).toIntArray())
        assertArrayEquals("spellInteger digitStr 42", intArrayOf(R.string.num_forty, R.string.num_two), speller.spellInteger(42).toIntArray())
        assertArrayEquals("spellInteger digitStr 43", intArrayOf(R.string.num_forty, R.string.num_three), speller.spellInteger(43).toIntArray())
        assertArrayEquals("spellInteger digitStr 44", intArrayOf(R.string.num_forty, R.string.num_four), speller.spellInteger(44).toIntArray())
        assertArrayEquals("spellInteger digitStr 45", intArrayOf(R.string.num_forty, R.string.num_five), speller.spellInteger(45).toIntArray())
        assertArrayEquals("spellInteger digitStr 46", intArrayOf(R.string.num_forty, R.string.num_six), speller.spellInteger(46).toIntArray())
        assertArrayEquals("spellInteger digitStr 47", intArrayOf(R.string.num_forty, R.string.num_seven), speller.spellInteger(47).toIntArray())
        assertArrayEquals("spellInteger digitStr 48", intArrayOf(R.string.num_forty, R.string.num_eight), speller.spellInteger(48).toIntArray())
        assertArrayEquals("spellInteger digitStr 49", intArrayOf(R.string.num_forty, R.string.num_nine), speller.spellInteger(49).toIntArray())
    }

    @Test
    fun testSpellFifties() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 50", intArrayOf(R.string.num_fifty), speller.spellInteger(50).toIntArray())
        assertArrayEquals("spellInteger digitStr 51", intArrayOf(R.string.num_fifty, R.string.num_one), speller.spellInteger(51).toIntArray())
        assertArrayEquals("spellInteger digitStr 52", intArrayOf(R.string.num_fifty, R.string.num_two), speller.spellInteger(52).toIntArray())
        assertArrayEquals("spellInteger digitStr 53", intArrayOf(R.string.num_fifty, R.string.num_three), speller.spellInteger(53).toIntArray())
        assertArrayEquals("spellInteger digitStr 54", intArrayOf(R.string.num_fifty, R.string.num_four), speller.spellInteger(54).toIntArray())
        assertArrayEquals("spellInteger digitStr 55", intArrayOf(R.string.num_fifty, R.string.num_five), speller.spellInteger(55).toIntArray())
        assertArrayEquals("spellInteger digitStr 56", intArrayOf(R.string.num_fifty, R.string.num_six), speller.spellInteger(56).toIntArray())
        assertArrayEquals("spellInteger digitStr 57", intArrayOf(R.string.num_fifty, R.string.num_seven), speller.spellInteger(57).toIntArray())
        assertArrayEquals("spellInteger digitStr 58", intArrayOf(R.string.num_fifty, R.string.num_eight), speller.spellInteger(58).toIntArray())
        assertArrayEquals("spellInteger digitStr 59", intArrayOf(R.string.num_fifty, R.string.num_nine), speller.spellInteger(59).toIntArray())
    }

    @Test
    fun testSpellNineties() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 90", intArrayOf(R.string.num_ninety), speller.spellInteger(90).toIntArray())
        assertArrayEquals("spellInteger digitStr 91", intArrayOf(R.string.num_ninety, R.string.num_one), speller.spellInteger(91).toIntArray())
        assertArrayEquals("spellInteger digitStr 92", intArrayOf(R.string.num_ninety, R.string.num_two), speller.spellInteger(92).toIntArray())
        assertArrayEquals("spellInteger digitStr 93", intArrayOf(R.string.num_ninety, R.string.num_three), speller.spellInteger(93).toIntArray())
        assertArrayEquals("spellInteger digitStr 94", intArrayOf(R.string.num_ninety, R.string.num_four), speller.spellInteger(94).toIntArray())
        assertArrayEquals("spellInteger digitStr 95", intArrayOf(R.string.num_ninety, R.string.num_five), speller.spellInteger(95).toIntArray())
        assertArrayEquals("spellInteger digitStr 96", intArrayOf(R.string.num_ninety, R.string.num_six), speller.spellInteger(96).toIntArray())
        assertArrayEquals("spellInteger digitStr 97", intArrayOf(R.string.num_ninety, R.string.num_seven), speller.spellInteger(97).toIntArray())
        assertArrayEquals("spellInteger digitStr 98", intArrayOf(R.string.num_ninety, R.string.num_eight), speller.spellInteger(98).toIntArray())
        assertArrayEquals("spellInteger digitStr 99", intArrayOf(R.string.num_ninety, R.string.num_nine), speller.spellInteger(99).toIntArray())
    }

    @Test
    fun testSpellHundreds() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 100", intArrayOf(R.string.num_one, R.string.num_hundred), speller.spellInteger(100).toIntArray())
        assertArrayEquals("spellInteger digitStr 101", intArrayOf(R.string.num_one, R.string.num_hundred, R.string.num_one), speller.spellInteger(101).toIntArray())
        assertArrayEquals("spellInteger digitStr 110", intArrayOf(R.string.num_one, R.string.num_hundred, R.string.num_ten), speller.spellInteger(110).toIntArray())
        assertArrayEquals("spellInteger digitStr 200", intArrayOf(R.string.num_two, R.string.num_hundred), speller.spellInteger(200).toIntArray())
        assertArrayEquals("spellInteger digitStr 219", intArrayOf(R.string.num_two, R.string.num_hundred, R.string.num_nineteen), speller.spellInteger(219).toIntArray())
        assertArrayEquals("spellInteger digitStr 999", intArrayOf(R.string.num_nine, R.string.num_hundred, R.string.num_ninety, R.string.num_nine), speller.spellInteger(999).toIntArray())
    }

    @Test
    fun testSpellThousands() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 1000", intArrayOf(R.string.num_one, R.string.num_thousand), speller.spellInteger(1000).toIntArray())
        assertArrayEquals("spellInteger digitStr 1001", intArrayOf(R.string.num_one, R.string.num_thousand, R.string.num_one), speller.spellInteger(1001).toIntArray())
        assertArrayEquals("spellInteger digitStr 10000", intArrayOf(R.string.num_ten, R.string.num_thousand), speller.spellInteger(10000).toIntArray())
        assertArrayEquals("spellInteger digitStr 10015", intArrayOf(R.string.num_ten, R.string.num_thousand, R.string.num_fifteen), speller.spellInteger(10015).toIntArray())
        assertArrayEquals("spellInteger digitStr 10020", intArrayOf(R.string.num_ten, R.string.num_thousand, R.string.num_twenty), speller.spellInteger(10020).toIntArray())
        assertArrayEquals("spellInteger digitStr 10021", intArrayOf(R.string.num_ten, R.string.num_thousand, R.string.num_twenty, R.string.num_one), speller.spellInteger(10021).toIntArray())
        assertArrayEquals("spellInteger digitStr 999999", intArrayOf(R.string.num_nine, R.string.num_hundred, R.string.num_ninety, R.string.num_nine,
                                                            R.string.num_thousand, R.string.num_nine, R.string.num_hundred, R.string.num_ninety, R.string.num_nine),
                                                            speller.spellInteger(999999).toIntArray())
    }

    @Test
    fun testSpellMillions() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 1,000,000", intArrayOf(R.string.num_one, R.string.num_million), speller.spellInteger(1000*1000).toIntArray())
        assertArrayEquals("spellInteger digitStr 1,100,000", intArrayOf(R.string.num_one, R.string.num_million,
                R.string.num_one, R.string.num_hundred, R.string.num_thousand), speller.spellInteger(1000*1000+100*1000).toIntArray())
    }

    @Test
    fun testSpellBillion() {
        val speller = NumberSpeller()

        assertArrayEquals("spellInteger digitStr 1,000,000,000", intArrayOf(R.string.num_one, R.string.num_billion), speller.spellInteger(1000*1000*1000).toIntArray())
        assertArrayEquals("spellInteger digitStr 1,100,000,000", intArrayOf(R.string.num_one, R.string.num_billion,
                R.string.num_one, R.string.num_hundred, R.string.num_million), speller.spellInteger(1000*1000*1000+100*1000*1000).toIntArray())
    }
}
