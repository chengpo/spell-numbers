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

    private inline fun EnglishNumberSpeller.verify(sampleData: () -> Map<Int, List<String>>) {
        sampleData().forEach { number, words ->
            spellInteger(number.toLong()).toString() shouldEqual  words.joinToString(separator = " ")
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 20 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(0 to listOf("Zero"),
                        1 to listOf("One"),
                        2 to listOf("Two"),
                        3 to listOf("Three"),
                        4 to listOf("Four"),
                        5 to listOf("Five"),
                        6 to listOf("Six"),
                        7 to listOf("Seven"),
                        8 to listOf("Eight"),
                        9 to listOf("Nine"),
                        10 to listOf("Ten"),
                        11 to listOf("Eleven"),
                        12 to listOf("Twelve"),
                        13 to listOf("Thirteen"),
                        14 to listOf("Fourteen"),
                        15 to listOf("Fifteen"),
                        16 to listOf("Sixteen"),
                        17 to listOf("Seventeen"),
                        18 to listOf("Eighteen"),
                        19 to listOf("Nineteen"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 30 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(20 to listOf("Twenty"),
                        21 to listOf("Twenty", "One"),
                        22 to listOf("Twenty", "Two"),
                        23 to listOf("Twenty", "Three"),
                        24 to listOf("Twenty", "Four"),
                        25 to listOf("Twenty", "Five"),
                        26 to listOf("Twenty", "Six"),
                        27 to listOf("Twenty", "Seven"),
                        28 to listOf("Twenty", "Eight"),
                        29 to listOf("Twenty", "Nine"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 40 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(30 to listOf("Thirty"),
                        31 to listOf("Thirty", "One"),
                        32 to listOf("Thirty", "Two"),
                        33 to listOf("Thirty", "Three"),
                        34 to listOf("Thirty", "Four"),
                        35 to listOf("Thirty", "Five"),
                        36 to listOf("Thirty", "Six"),
                        37 to listOf("Thirty", "Seven"),
                        38 to listOf("Thirty", "Eight"),
                        39 to listOf("Thirty", "Nine")
                )
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 50 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(40 to listOf("Forty"),
                        41 to listOf("Forty", "One"),
                        42 to listOf("Forty", "Two"),
                        43 to listOf("Forty", "Three"),
                        44 to listOf("Forty", "Four"),
                        45 to listOf("Forty", "Five"),
                        46 to listOf("Forty", "Six"),
                        47 to listOf("Forty", "Seven"),
                        48 to listOf("Forty", "Eight"),
                        49 to listOf("Forty", "Nine"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 60 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(50 to listOf("Fifty"),
                        51 to listOf("Fifty", "One"),
                        52 to listOf("Fifty", "Two"),
                        53 to listOf("Fifty", "Three"),
                        54 to listOf("Fifty", "Four"),
                        55 to listOf("Fifty", "Five"),
                        56 to listOf("Fifty", "Six"),
                        57 to listOf("Fifty", "Seven"),
                        58 to listOf("Fifty", "Eight"),
                        59 to listOf("Fifty", "Nine"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number less than 100 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(90 to listOf("Ninety"),
                        91 to listOf("Ninety", "One"),
                        92 to listOf("Ninety", "Two"),
                        93 to listOf("Ninety", "Three"),
                        94 to listOf("Ninety", "Four"),
                        95 to listOf("Ninety", "Five"),
                        96 to listOf("Ninety", "Six"),
                        97 to listOf("Ninety", "Seven"),
                        98 to listOf("Ninety", "Eight"),
                        99 to listOf("Ninety", "Nine")
                )
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number larger than 100 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(100 to listOf("One", "Hundred"),
                        101 to listOf("One", "Hundred", "One"),
                        110 to listOf("One", "Hundred", "Ten"),
                        200 to listOf("Two", "Hundred"),
                        219 to listOf("Two", "Hundred", "Nineteen"),
                        999 to listOf("Nine", "Hundred", "Ninety", "Nine")
                )
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number larger than 1000 correct`() {
        englishNumberSpeller {
            verify {
                mapOf(1000 to listOf("One", "Thousand"),
                        1001 to listOf("One", "Thousand", "One"),
                        10000 to listOf("Ten", "Thousand"),
                        10015 to listOf("Ten", "Thousand", "Fifteen"),
                        10020 to listOf("Ten", "Thousand", "Twenty"),
                        10021 to listOf("Ten", "Thousand", "Twenty", "One"),
                        999999 to listOf("Nine", "Hundred", "Ninety", "Nine",
                                "Thousand", "Nine", "Hundred", "Ninety", "Nine"))
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number larger than 1 million`() {
        englishNumberSpeller {
            verify {
                mapOf(1000 * 1000 to listOf("One", "Million"),
                        1000 * 1000 + 100 * 1000 to listOf("One", "Million", "One", "Hundred", "Thousand")
                )
            }
        }
    }

    @Test
    fun `englishNumberSpeller should spell number larger than 1 billion`() {
        englishNumberSpeller {
            verify {
                mapOf(1000 * 1000 * 1000 to listOf("One", "Billion"),
                        1000 * 1000 * 1000 + 100 * 1000 * 1000 to listOf("One", "Billion", "One", "Hundred", "Million"))
            }
        }
    }
}
