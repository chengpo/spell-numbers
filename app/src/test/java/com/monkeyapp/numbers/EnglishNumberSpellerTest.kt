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

import com.monkeyapp.numbers.translators.EnglishNumberSpeller
import org.junit.Test

import org.junit.Assert.*


class EnglishNumberSpellerTest {
    @Test
    fun testSpellTens() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 0", arrayOf("Zero"), speller.spellInteger(0))
        assertArrayEquals("spellInteger digitStr 1", arrayOf("One"), speller.spellInteger(1))
        assertArrayEquals("spellInteger digitStr 2", arrayOf("Two"), speller.spellInteger(2))
        assertArrayEquals("spellInteger digitStr 3", arrayOf("Three"), speller.spellInteger(3))
        assertArrayEquals("spellInteger digitStr 4", arrayOf("Four"), speller.spellInteger(4))
        assertArrayEquals("spellInteger digitStr 5", arrayOf("Five"), speller.spellInteger(5))
        assertArrayEquals("spellInteger digitStr 6", arrayOf("Six"), speller.spellInteger(6))
        assertArrayEquals("spellInteger digitStr 7", arrayOf("Seven"), speller.spellInteger(7))
        assertArrayEquals("spellInteger digitStr 8", arrayOf("Eight"), speller.spellInteger(8))
        assertArrayEquals("spellInteger digitStr 9", arrayOf("Nine"), speller.spellInteger(9))
        assertArrayEquals("spellInteger digitStr 10", arrayOf("Ten"), speller.spellInteger(10))
        assertArrayEquals("spellInteger digitStr 11", arrayOf("Eleven"), speller.spellInteger(11))
        assertArrayEquals("spellInteger digitStr 12", arrayOf("Twelve"), speller.spellInteger(12))
        assertArrayEquals("spellInteger digitStr 13", arrayOf("Thirteen"), speller.spellInteger(13))
        assertArrayEquals("spellInteger digitStr 14", arrayOf("Fourteen"), speller.spellInteger(14))
        assertArrayEquals("spellInteger digitStr 15", arrayOf("Fifteen"), speller.spellInteger(15))
        assertArrayEquals("spellInteger digitStr 16", arrayOf("Sixteen"), speller.spellInteger(16))
        assertArrayEquals("spellInteger digitStr 17", arrayOf("Seventeen"), speller.spellInteger(17))
        assertArrayEquals("spellInteger digitStr 18", arrayOf("Eighteen"), speller.spellInteger(18))
        assertArrayEquals("spellInteger digitStr 19", arrayOf("Nineteen"), speller.spellInteger(19))
    }

    @Test
    fun testSpellTwenties() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 20", arrayOf("Twenty"), speller.spellInteger(20))
        assertArrayEquals("spellInteger digitStr 21", arrayOf("Twenty", "One"), speller.spellInteger(21))
        assertArrayEquals("spellInteger digitStr 22", arrayOf("Twenty", "Two"), speller.spellInteger(22))
        assertArrayEquals("spellInteger digitStr 23", arrayOf("Twenty", "Three"), speller.spellInteger(23))
        assertArrayEquals("spellInteger digitStr 24", arrayOf("Twenty", "Four"), speller.spellInteger(24))
        assertArrayEquals("spellInteger digitStr 25", arrayOf("Twenty", "Five"), speller.spellInteger(25))
        assertArrayEquals("spellInteger digitStr 26", arrayOf("Twenty", "Six"), speller.spellInteger(26))
        assertArrayEquals("spellInteger digitStr 27", arrayOf("Twenty", "Seven"), speller.spellInteger(27))
        assertArrayEquals("spellInteger digitStr 28", arrayOf("Twenty", "Eight"), speller.spellInteger(28))
        assertArrayEquals("spellInteger digitStr 29", arrayOf("Twenty", "Nine"), speller.spellInteger(29))
    }

    @Test
    fun testSpellThrties() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 30", arrayOf("Thirty"), speller.spellInteger(30))
        assertArrayEquals("spellInteger digitStr 31", arrayOf("Thirty", "One"), speller.spellInteger(31))
        assertArrayEquals("spellInteger digitStr 32", arrayOf("Thirty", "Two"), speller.spellInteger(32))
        assertArrayEquals("spellInteger digitStr 33", arrayOf("Thirty", "Three"), speller.spellInteger(33))
        assertArrayEquals("spellInteger digitStr 34", arrayOf("Thirty", "Four"), speller.spellInteger(34))
        assertArrayEquals("spellInteger digitStr 35", arrayOf("Thirty", "Five"), speller.spellInteger(35))
        assertArrayEquals("spellInteger digitStr 36", arrayOf("Thirty", "Six"), speller.spellInteger(36))
        assertArrayEquals("spellInteger digitStr 37", arrayOf("Thirty", "Seven"), speller.spellInteger(37))
        assertArrayEquals("spellInteger digitStr 38", arrayOf("Thirty", "Eight"), speller.spellInteger(38))
        assertArrayEquals("spellInteger digitStr 39", arrayOf("Thirty", "Nine"), speller.spellInteger(39))
    }

    @Test
    fun testSpellForties() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 40", arrayOf("Forty"), speller.spellInteger(40))
        assertArrayEquals("spellInteger digitStr 41", arrayOf("Forty", "One"), speller.spellInteger(41))
        assertArrayEquals("spellInteger digitStr 42", arrayOf("Forty", "Two"), speller.spellInteger(42))
        assertArrayEquals("spellInteger digitStr 43", arrayOf("Forty", "Three"), speller.spellInteger(43))
        assertArrayEquals("spellInteger digitStr 44", arrayOf("Forty", "Four"), speller.spellInteger(44))
        assertArrayEquals("spellInteger digitStr 45", arrayOf("Forty", "Five"), speller.spellInteger(45))
        assertArrayEquals("spellInteger digitStr 46", arrayOf("Forty", "Six"), speller.spellInteger(46))
        assertArrayEquals("spellInteger digitStr 47", arrayOf("Forty", "Seven"), speller.spellInteger(47))
        assertArrayEquals("spellInteger digitStr 48", arrayOf("Forty", "Eight"), speller.spellInteger(48))
        assertArrayEquals("spellInteger digitStr 49", arrayOf("Forty", "Nine"), speller.spellInteger(49))
    }

    @Test
    fun testSpellFifties() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 50", arrayOf("Fifty"), speller.spellInteger(50))
        assertArrayEquals("spellInteger digitStr 51", arrayOf("Fifty", "One"), speller.spellInteger(51))
        assertArrayEquals("spellInteger digitStr 52", arrayOf("Fifty", "Two"), speller.spellInteger(52))
        assertArrayEquals("spellInteger digitStr 53", arrayOf("Fifty", "Three"), speller.spellInteger(53))
        assertArrayEquals("spellInteger digitStr 54", arrayOf("Fifty", "Four"), speller.spellInteger(54))
        assertArrayEquals("spellInteger digitStr 55", arrayOf("Fifty", "Five"), speller.spellInteger(55))
        assertArrayEquals("spellInteger digitStr 56", arrayOf("Fifty", "Six"), speller.spellInteger(56))
        assertArrayEquals("spellInteger digitStr 57", arrayOf("Fifty", "Seven"), speller.spellInteger(57))
        assertArrayEquals("spellInteger digitStr 58", arrayOf("Fifty", "Eight"), speller.spellInteger(58))
        assertArrayEquals("spellInteger digitStr 59", arrayOf("Fifty", "Nine"), speller.spellInteger(59))
    }

    @Test
    fun testSpellNineties() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 90", arrayOf("Ninety"), speller.spellInteger(90))
        assertArrayEquals("spellInteger digitStr 91", arrayOf("Ninety", "One"), speller.spellInteger(91))
        assertArrayEquals("spellInteger digitStr 92", arrayOf("Ninety", "Two"), speller.spellInteger(92))
        assertArrayEquals("spellInteger digitStr 93", arrayOf("Ninety", "Three"), speller.spellInteger(93))
        assertArrayEquals("spellInteger digitStr 94", arrayOf("Ninety", "Four"), speller.spellInteger(94))
        assertArrayEquals("spellInteger digitStr 95", arrayOf("Ninety", "Five"), speller.spellInteger(95))
        assertArrayEquals("spellInteger digitStr 96", arrayOf("Ninety", "Six"), speller.spellInteger(96))
        assertArrayEquals("spellInteger digitStr 97", arrayOf("Ninety", "Seven"), speller.spellInteger(97))
        assertArrayEquals("spellInteger digitStr 98", arrayOf("Ninety", "Eight"), speller.spellInteger(98))
        assertArrayEquals("spellInteger digitStr 99", arrayOf("Ninety", "Nine"), speller.spellInteger(99))
    }

    @Test
    fun testSpellHundreds() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 100", arrayOf("One", "Hundred"), speller.spellInteger(100))
        assertArrayEquals("spellInteger digitStr 101", arrayOf("One", "Hundred", "One"), speller.spellInteger(101))
        assertArrayEquals("spellInteger digitStr 110", arrayOf("One", "Hundred", "Ten"), speller.spellInteger(110))
        assertArrayEquals("spellInteger digitStr 200", arrayOf("Two", "Hundred"), speller.spellInteger(200))
        assertArrayEquals("spellInteger digitStr 219", arrayOf("Two", "Hundred", "Nineteen"), speller.spellInteger(219))
        assertArrayEquals("spellInteger digitStr 999", arrayOf("Nine", "Hundred", "Ninety", "Nine"), speller.spellInteger(999))
    }

    @Test
    fun testSpellThousands() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 1000", arrayOf("One", "Thousand"), speller.spellInteger(1000))
        assertArrayEquals("spellInteger digitStr 1001", arrayOf("One", "Thousand", "One"), speller.spellInteger(1001))
        assertArrayEquals("spellInteger digitStr 10000", arrayOf("Ten", "Thousand"), speller.spellInteger(10000))
        assertArrayEquals("spellInteger digitStr 10015", arrayOf("Ten", "Thousand", "Fifteen"), speller.spellInteger(10015))
        assertArrayEquals("spellInteger digitStr 10020", arrayOf("Ten", "Thousand", "Twenty"), speller.spellInteger(10020))
        assertArrayEquals("spellInteger digitStr 10021", arrayOf("Ten", "Thousand", "Twenty", "One"), speller.spellInteger(10021))
        assertArrayEquals("spellInteger digitStr 999999", arrayOf("Nine", "Hundred", "Ninety", "Nine",
                                                            "Thousand", "Nine", "Hundred", "Ninety", "Nine"),
                                                            speller.spellInteger(999999))
    }

    @Test
    fun testSpellMillions() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 1,000,000", arrayOf("One", "Million"), speller.spellInteger(1000*1000))
        assertArrayEquals("spellInteger digitStr 1,100,000", arrayOf("One", "Million",
                "One", "Hundred", "Thousand"), speller.spellInteger(1000*1000+100*1000))
    }

    @Test
    fun testSpellBillion() {
        val speller = EnglishNumberSpeller()

        assertArrayEquals("spellInteger digitStr 1,000,000,000", arrayOf("One", "Billion"), speller.spellInteger(1000*1000*1000))
        assertArrayEquals("spellInteger digitStr 1,100,000,000", arrayOf("One", "Billion",
                "One", "Hundred", "Million"), speller.spellInteger(1000*1000*1000+100*1000*1000))
    }
}
