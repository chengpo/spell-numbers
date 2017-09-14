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

        assertArrayEquals("spell number 0", intArrayOf(R.string.num_zero), speller.spell(0).toIntArray())
        assertArrayEquals("spell number 1", intArrayOf(R.string.num_one), speller.spell(1).toIntArray())
        assertArrayEquals("spell number 2", intArrayOf(R.string.num_two), speller.spell(2).toIntArray())
        assertArrayEquals("spell number 3", intArrayOf(R.string.num_three), speller.spell(3).toIntArray())
        assertArrayEquals("spell number 4", intArrayOf(R.string.num_four), speller.spell(4).toIntArray())
        assertArrayEquals("spell number 5", intArrayOf(R.string.num_five), speller.spell(5).toIntArray())
        assertArrayEquals("spell number 6", intArrayOf(R.string.num_six), speller.spell(6).toIntArray())
        assertArrayEquals("spell number 7", intArrayOf(R.string.num_seven), speller.spell(7).toIntArray())
        assertArrayEquals("spell number 8", intArrayOf(R.string.num_eight), speller.spell(8).toIntArray())
        assertArrayEquals("spell number 9", intArrayOf(R.string.num_nine), speller.spell(9).toIntArray())
        assertArrayEquals("spell number 10", intArrayOf(R.string.num_ten), speller.spell(10).toIntArray())
        assertArrayEquals("spell number 11", intArrayOf(R.string.num_eleven), speller.spell(11).toIntArray())
        assertArrayEquals("spell number 12", intArrayOf(R.string.num_twelve), speller.spell(12).toIntArray())
        assertArrayEquals("spell number 13", intArrayOf(R.string.num_thirteen), speller.spell(13).toIntArray())
        assertArrayEquals("spell number 14", intArrayOf(R.string.num_fourteen), speller.spell(14).toIntArray())
        assertArrayEquals("spell number 15", intArrayOf(R.string.num_fifteen), speller.spell(15).toIntArray())
        assertArrayEquals("spell number 16", intArrayOf(R.string.num_sixteen), speller.spell(16).toIntArray())
        assertArrayEquals("spell number 17", intArrayOf(R.string.num_seventeen), speller.spell(17).toIntArray())
        assertArrayEquals("spell number 18", intArrayOf(R.string.num_eighteen), speller.spell(18).toIntArray())
        assertArrayEquals("spell number 19", intArrayOf(R.string.num_nineteen), speller.spell(19).toIntArray())
    }

    @Test
    fun testSpellTwenties() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 20", intArrayOf(R.string.num_twenty), speller.spell(20).toIntArray())
        assertArrayEquals("spell number 21", intArrayOf(R.string.num_twenty, R.string.num_one), speller.spell(21).toIntArray())
        assertArrayEquals("spell number 22", intArrayOf(R.string.num_twenty, R.string.num_two), speller.spell(22).toIntArray())
        assertArrayEquals("spell number 23", intArrayOf(R.string.num_twenty, R.string.num_three), speller.spell(23).toIntArray())
        assertArrayEquals("spell number 24", intArrayOf(R.string.num_twenty, R.string.num_four), speller.spell(24).toIntArray())
        assertArrayEquals("spell number 25", intArrayOf(R.string.num_twenty, R.string.num_five), speller.spell(25).toIntArray())
        assertArrayEquals("spell number 26", intArrayOf(R.string.num_twenty, R.string.num_six), speller.spell(26).toIntArray())
        assertArrayEquals("spell number 27", intArrayOf(R.string.num_twenty, R.string.num_seven), speller.spell(27).toIntArray())
        assertArrayEquals("spell number 28", intArrayOf(R.string.num_twenty, R.string.num_eight), speller.spell(28).toIntArray())
        assertArrayEquals("spell number 29", intArrayOf(R.string.num_twenty, R.string.num_nine), speller.spell(29).toIntArray())
    }

    @Test
    fun testSpellThrties() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 30", intArrayOf(R.string.num_thirty), speller.spell(30).toIntArray())
        assertArrayEquals("spell number 31", intArrayOf(R.string.num_thirty, R.string.num_one), speller.spell(31).toIntArray())
        assertArrayEquals("spell number 32", intArrayOf(R.string.num_thirty, R.string.num_two), speller.spell(32).toIntArray())
        assertArrayEquals("spell number 33", intArrayOf(R.string.num_thirty, R.string.num_three), speller.spell(33).toIntArray())
        assertArrayEquals("spell number 34", intArrayOf(R.string.num_thirty, R.string.num_four), speller.spell(34).toIntArray())
        assertArrayEquals("spell number 35", intArrayOf(R.string.num_thirty, R.string.num_five), speller.spell(35).toIntArray())
        assertArrayEquals("spell number 36", intArrayOf(R.string.num_thirty, R.string.num_six), speller.spell(36).toIntArray())
        assertArrayEquals("spell number 37", intArrayOf(R.string.num_thirty, R.string.num_seven), speller.spell(37).toIntArray())
        assertArrayEquals("spell number 38", intArrayOf(R.string.num_thirty, R.string.num_eight), speller.spell(38).toIntArray())
        assertArrayEquals("spell number 39", intArrayOf(R.string.num_thirty, R.string.num_nine), speller.spell(39).toIntArray())
    }

    @Test
    fun testSpellForties() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 40", intArrayOf(R.string.num_forty), speller.spell(40).toIntArray())
        assertArrayEquals("spell number 41", intArrayOf(R.string.num_forty, R.string.num_one), speller.spell(41).toIntArray())
        assertArrayEquals("spell number 42", intArrayOf(R.string.num_forty, R.string.num_two), speller.spell(42).toIntArray())
        assertArrayEquals("spell number 43", intArrayOf(R.string.num_forty, R.string.num_three), speller.spell(43).toIntArray())
        assertArrayEquals("spell number 44", intArrayOf(R.string.num_forty, R.string.num_four), speller.spell(44).toIntArray())
        assertArrayEquals("spell number 45", intArrayOf(R.string.num_forty, R.string.num_five), speller.spell(45).toIntArray())
        assertArrayEquals("spell number 46", intArrayOf(R.string.num_forty, R.string.num_six), speller.spell(46).toIntArray())
        assertArrayEquals("spell number 47", intArrayOf(R.string.num_forty, R.string.num_seven), speller.spell(47).toIntArray())
        assertArrayEquals("spell number 48", intArrayOf(R.string.num_forty, R.string.num_eight), speller.spell(48).toIntArray())
        assertArrayEquals("spell number 49", intArrayOf(R.string.num_forty, R.string.num_nine), speller.spell(49).toIntArray())
    }

    @Test
    fun testSpellFifties() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 50", intArrayOf(R.string.num_fifty), speller.spell(50).toIntArray())
        assertArrayEquals("spell number 51", intArrayOf(R.string.num_fifty, R.string.num_one), speller.spell(51).toIntArray())
        assertArrayEquals("spell number 52", intArrayOf(R.string.num_fifty, R.string.num_two), speller.spell(52).toIntArray())
        assertArrayEquals("spell number 53", intArrayOf(R.string.num_fifty, R.string.num_three), speller.spell(53).toIntArray())
        assertArrayEquals("spell number 54", intArrayOf(R.string.num_fifty, R.string.num_four), speller.spell(54).toIntArray())
        assertArrayEquals("spell number 55", intArrayOf(R.string.num_fifty, R.string.num_five), speller.spell(55).toIntArray())
        assertArrayEquals("spell number 56", intArrayOf(R.string.num_fifty, R.string.num_six), speller.spell(56).toIntArray())
        assertArrayEquals("spell number 57", intArrayOf(R.string.num_fifty, R.string.num_seven), speller.spell(57).toIntArray())
        assertArrayEquals("spell number 58", intArrayOf(R.string.num_fifty, R.string.num_eight), speller.spell(58).toIntArray())
        assertArrayEquals("spell number 59", intArrayOf(R.string.num_fifty, R.string.num_nine), speller.spell(59).toIntArray())
    }

    @Test
    fun testSpellNineties() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 90", intArrayOf(R.string.num_ninety), speller.spell(90).toIntArray())
        assertArrayEquals("spell number 91", intArrayOf(R.string.num_ninety, R.string.num_one), speller.spell(91).toIntArray())
        assertArrayEquals("spell number 92", intArrayOf(R.string.num_ninety, R.string.num_two), speller.spell(92).toIntArray())
        assertArrayEquals("spell number 93", intArrayOf(R.string.num_ninety, R.string.num_three), speller.spell(93).toIntArray())
        assertArrayEquals("spell number 94", intArrayOf(R.string.num_ninety, R.string.num_four), speller.spell(94).toIntArray())
        assertArrayEquals("spell number 95", intArrayOf(R.string.num_ninety, R.string.num_five), speller.spell(95).toIntArray())
        assertArrayEquals("spell number 96", intArrayOf(R.string.num_ninety, R.string.num_six), speller.spell(96).toIntArray())
        assertArrayEquals("spell number 97", intArrayOf(R.string.num_ninety, R.string.num_seven), speller.spell(97).toIntArray())
        assertArrayEquals("spell number 98", intArrayOf(R.string.num_ninety, R.string.num_eight), speller.spell(98).toIntArray())
        assertArrayEquals("spell number 99", intArrayOf(R.string.num_ninety, R.string.num_nine), speller.spell(99).toIntArray())
    }

    @Test
    fun testSpellHundreds() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 100", intArrayOf(R.string.num_one, R.string.num_hundred), speller.spell(100).toIntArray())
        assertArrayEquals("spell number 101", intArrayOf(R.string.num_one, R.string.num_hundred, R.string.num_one), speller.spell(101).toIntArray())
        assertArrayEquals("spell number 110", intArrayOf(R.string.num_one, R.string.num_hundred, R.string.num_ten), speller.spell(110).toIntArray())
        assertArrayEquals("spell number 200", intArrayOf(R.string.num_two, R.string.num_hundred), speller.spell(200).toIntArray())
        assertArrayEquals("spell number 219", intArrayOf(R.string.num_two, R.string.num_hundred, R.string.num_nineteen), speller.spell(219).toIntArray())
        assertArrayEquals("spell number 999", intArrayOf(R.string.num_nine, R.string.num_hundred, R.string.num_ninety, R.string.num_nine), speller.spell(999).toIntArray())
    }

    @Test
    fun testSpellThousands() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 1000", intArrayOf(R.string.num_one, R.string.num_thousand), speller.spell(1000).toIntArray())
        assertArrayEquals("spell number 1001", intArrayOf(R.string.num_one, R.string.num_thousand, R.string.num_one), speller.spell(1001).toIntArray())
        assertArrayEquals("spell number 10000", intArrayOf(R.string.num_ten, R.string.num_thousand), speller.spell(10000).toIntArray())
        assertArrayEquals("spell number 10015", intArrayOf(R.string.num_ten, R.string.num_thousand, R.string.num_fifteen), speller.spell(10015).toIntArray())
        assertArrayEquals("spell number 10020", intArrayOf(R.string.num_ten, R.string.num_thousand, R.string.num_twenty), speller.spell(10020).toIntArray())
        assertArrayEquals("spell number 10021", intArrayOf(R.string.num_ten, R.string.num_thousand, R.string.num_twenty, R.string.num_one), speller.spell(10021).toIntArray())
        assertArrayEquals("spell number 999999", intArrayOf(R.string.num_nine, R.string.num_hundred, R.string.num_ninety, R.string.num_nine,
                                                            R.string.num_thousand, R.string.num_nine, R.string.num_hundred, R.string.num_ninety, R.string.num_nine),
                                                            speller.spell(999999).toIntArray())
    }

    @Test
    fun testSpellMillions() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 1,000,000", intArrayOf(R.string.num_one, R.string.num_million), speller.spell(1000*1000).toIntArray())
        assertArrayEquals("spell number 1,100,000", intArrayOf(R.string.num_one, R.string.num_million,
                R.string.num_one, R.string.num_hundred, R.string.num_thousand), speller.spell(1000*1000+100*1000).toIntArray())
    }

    @Test
    fun testSpellBillion() {
        val speller = NumberSpeller()

        assertArrayEquals("spell number 1,000,000,000", intArrayOf(R.string.num_one, R.string.num_billion), speller.spell(1000*1000*1000).toIntArray())
        assertArrayEquals("spell number 1,100,000,000", intArrayOf(R.string.num_one, R.string.num_billion,
                R.string.num_one, R.string.num_hundred, R.string.num_million), speller.spell(1000*1000*1000+100*1000*1000).toIntArray())
    }
}
