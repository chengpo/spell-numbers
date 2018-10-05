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

package com.monkeyapp.numbers

import com.monkeyapp.numbers.translators.EnglishNumberSpeller
import org.junit.Test

class EnglishNumberSpellerTest {
    private inline fun englishNumberSpeller(action: EnglishNumberSpeller.() -> Unit) = action(EnglishNumberSpeller())

    private inline fun EnglishNumberSpeller.verify(sampleData: () -> Map<Int, Array<String>>) {
        sampleData().forEach { number, words ->
            spellInteger(number.toLong()) shouldEqual words
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 20 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(0 to arrayOf("Zero"),
                        1 to arrayOf("One"),
                        2 to arrayOf("Two"),
                        3 to arrayOf("Three"),
                        4 to arrayOf("Four"),
                        5 to arrayOf("Five"),
                        6 to arrayOf("Six"),
                        7 to arrayOf("Seven"),
                        8 to arrayOf("Eight"),
                        9 to arrayOf("Nine"),
                        10 to arrayOf("Ten"),
                        11 to arrayOf("Eleven"),
                        12 to arrayOf("Twelve"),
                        13 to arrayOf("Thirteen"),
                        14 to arrayOf("Fourteen"),
                        15 to arrayOf("Fifteen"),
                        16 to arrayOf("Sixteen"),
                        17 to arrayOf("Seventeen"),
                        18 to arrayOf("Eighteen"),
                        19 to arrayOf("Nineteen"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 30 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(20 to arrayOf("Twenty"),
                        21 to arrayOf("Twenty", "One"),
                        22 to arrayOf("Twenty", "Two"),
                        23 to arrayOf("Twenty", "Three"),
                        24 to arrayOf("Twenty", "Four"),
                        25 to arrayOf("Twenty", "Five"),
                        26 to arrayOf("Twenty", "Six"),
                        27 to arrayOf("Twenty", "Seven"),
                        28 to arrayOf("Twenty", "Eight"),
                        29 to arrayOf("Twenty", "Nine"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 40 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(30 to arrayOf("Thirty"),
                        31 to arrayOf("Thirty", "One"),
                        32 to arrayOf("Thirty", "Two"),
                        33 to arrayOf("Thirty", "Three"),
                        34 to arrayOf("Thirty", "Four"),
                        35 to arrayOf("Thirty", "Five"),
                        36 to arrayOf("Thirty", "Six"),
                        37 to arrayOf("Thirty", "Seven"),
                        38 to arrayOf("Thirty", "Eight"),
                        39 to arrayOf("Thirty", "Nine")
                )
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 50 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(40 to arrayOf("Forty"),
                        41 to arrayOf("Forty", "One"),
                        42 to arrayOf("Forty", "Two"),
                        43 to arrayOf("Forty", "Three"),
                        44 to arrayOf("Forty", "Four"),
                        45 to arrayOf("Forty", "Five"),
                        46 to arrayOf("Forty", "Six"),
                        47 to arrayOf("Forty", "Seven"),
                        48 to arrayOf("Forty", "Eight"),
                        49 to arrayOf("Forty", "Nine"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 60 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(50 to arrayOf("Fifty"),
                        51 to arrayOf("Fifty", "One"),
                        52 to arrayOf("Fifty", "Two"),
                        53 to arrayOf("Fifty", "Three"),
                        54 to arrayOf("Fifty", "Four"),
                        55 to arrayOf("Fifty", "Five"),
                        56 to arrayOf("Fifty", "Six"),
                        57 to arrayOf("Fifty", "Seven"),
                        58 to arrayOf("Fifty", "Eight"),
                        59 to arrayOf("Fifty", "Nine"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 100 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(90 to arrayOf("Ninety"),
                        91 to arrayOf("Ninety", "One"),
                        92 to arrayOf("Ninety", "Two"),
                        93 to arrayOf("Ninety", "Three"),
                        94 to arrayOf("Ninety", "Four"),
                        95 to arrayOf("Ninety", "Five"),
                        96 to arrayOf("Ninety", "Six"),
                        97 to arrayOf("Ninety", "Seven"),
                        98 to arrayOf("Ninety", "Eight"),
                        99 to arrayOf("Ninety", "Nine")
                )
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number larger than 100 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(100 to arrayOf("One", "Hundred"),
                        101 to arrayOf("One", "Hundred", "One"),
                        110 to arrayOf("One", "Hundred", "Ten"),
                        200 to arrayOf("Two", "Hundred"),
                        219 to arrayOf("Two", "Hundred", "Nineteen"),
                        999 to arrayOf("Nine", "Hundred", "Ninety", "Nine")
                )
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number larger than 1000 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(1000 to arrayOf("One", "Thousand"),
                        1001 to arrayOf("One", "Thousand", "One"),
                        10000 to arrayOf("Ten", "Thousand"),
                        10015 to arrayOf("Ten", "Thousand", "Fifteen"),
                        10020 to arrayOf("Ten", "Thousand", "Twenty"),
                        10021 to arrayOf("Ten", "Thousand", "Twenty", "One"),
                        999999 to arrayOf("Nine", "Hundred", "Ninety", "Nine",
                                "Thousand", "Nine", "Hundred", "Ninety", "Nine"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number larger than 1 million`() {
        englishNumberSpeller {
            verify {
                mapOf(1000 * 1000 to arrayOf("One", "Million"),
                        1000 * 1000 + 100 * 1000 to arrayOf("One", "Million", "One", "Hundred", "Thousand")
                )
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number larger than 1 billion`() {
        englishNumberSpeller {
            verify {
                mapOf(1000 * 1000 * 1000 to arrayOf("One", "Billion"),
                        1000 * 1000 * 1000 + 100 * 1000 * 1000 to arrayOf("One", "Billion", "One", "Hundred", "Million"))
            }
        }
    }
}
