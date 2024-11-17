/*
MIT License

Copyright (c) 2017 - 2024 Po Cheng

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
import arrow.core.Either
import com.monkeyapp.numbers.testhelpers.shouldEqual
import org.junit.Test

class EnglishNumberSpellerTest {
    private data class TestSample(var integer: Long = 0L,
                             var decimal: Float = 0.0F,
                             var expected: String = "")

    private fun testSample(action: TestSample.() -> Unit) = TestSample().apply(action)

    private fun verifySpellIntegers(vararg samples: TestSample) {
        samples.forEach { sample ->
            val englishNumber = EnglishNumber(String.format("%.4f", sample.integer.toDouble() + sample.decimal))
            val numberWordsText = englishNumber.spell()

            numberWordsText.isRight() shouldEqual true
            (numberWordsText as Either.Right).b shouldEqual sample.expected
        }
    }

    @Test
    fun `englishNumberSpeller should spell number 1 to 19 correctly`() {
        val symbols = listOf("Zero", "One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
                "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen")

        symbols.mapIndexed { index, symbol ->
            testSample {
                integer = index.toLong()
                expected = "$symbol and 00 / 100"
            }
        }.forEach { sample ->
            verifySpellIntegers(sample)
        }
    }

    @Test
    fun `englishNumberSpeller should spell number 20 to 99 correctly`() {
        val symbols = listOf("", "One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine")

        val radixSymbols = listOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty",
                "Seventy", "Eighty", "Ninety")

        (2..9).map { radix ->
            symbols.mapIndexed { index, symbol ->
                testSample {
                    integer = radix * 10L + index
                    expected = if (index == 0) "${radixSymbols[radix]} and 00 / 100" else "${radixSymbols[radix]} $symbol and 00 / 100"
                }
            }
        }.forEach { samples ->
            verifySpellIntegers(*samples.toTypedArray())
        }
    }

    @Test
    fun `englishNumberSpeller should spell x00 to x19 correctly`() {
        val symbols = listOf("", "One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
                "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen")

        (1..9).map { hundred ->
            symbols.mapIndexed { index, symbol ->
                testSample {
                    integer = hundred * 100L + index
                    expected = if (index == 0) "${symbols[hundred]} Hundred and 00 / 100" else "${symbols[hundred]} Hundred $symbol and 00 / 100"
                }
            }
        }.forEach { samples ->
            verifySpellIntegers(*samples.toTypedArray())
        }
    }

    @Test
    fun `englishNumberSpeller should spell x20 to x99 correctly`() {
        val symbols = listOf("", "One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine")

        val radixSymbols = listOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty",
                "Seventy", "Eighty", "Ninety")

        (1..9).map { hundred ->
            (2..9).map { radix ->
                symbols.mapIndexed { index, symbol ->
                    testSample {
                        integer = hundred * 100L + radix * 10L + index
                        expected = if (index == 0)
                            "${symbols[hundred]} Hundred ${radixSymbols[radix]} and 00 / 100"
                        else
                            "${symbols[hundred]} Hundred ${radixSymbols[radix]} $symbol and 00 / 100"
                    }
                }
            }
        }.flatten().map { samples ->
            verifySpellIntegers(*samples.toTypedArray())
        }
    }

    @Test
    fun `englishNumberSpeller should spell thousands correctly`() {
        val symbols = listOf("", "One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine")

        for (number in 1..9) {
            verifySpellIntegers(
                    testSample {
                        integer = number * 1000L
                        expected = "${symbols[number]} Thousand and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000L + 1
                        expected = "${symbols[number]} Thousand One and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000L + 12L
                        expected = "${symbols[number]} Thousand Twelve and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000L + 21L
                        expected = "${symbols[number]} Thousand Twenty One and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000L + 199L
                        expected = "${symbols[number]} Thousand One Hundred Ninety Nine and 00 / 100"
                    }
            )
        }

        verifySpellIntegers(
                testSample {
                    integer = 100 * 1000L
                    expected = "One Hundred Thousand and 00 / 100"
                },
                testSample {
                    integer = 210 * 1000L
                    expected = "Two Hundred Ten Thousand and 00 / 100"
                },
                testSample {
                    integer = 210 * 1000L + 300L + 13L
                    expected = "Two Hundred Ten Thousand Three Hundred Thirteen and 00 / 100"
                }
        )
    }

    @Test
    fun `englishNumberSpeller should spell millions correctly`() {
        val symbols = listOf("", "One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine")

        for (number in 1..9) {
            verifySpellIntegers(
                    testSample {
                        integer = number * 1000 * 1000L
                        expected = "${symbols[number]} Million and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000 * 1000L + number * 1000L
                        expected = "${symbols[number]} Million ${symbols[number]} Thousand and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000 * 1000L + number * 1000L + number * 100L
                        expected = "${symbols[number]} Million ${symbols[number]} Thousand ${symbols[number]} Hundred and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000 * 1000L + number * 1000L + number * 100L + number
                        expected = "${symbols[number]} Million ${symbols[number]} Thousand ${symbols[number]} Hundred ${symbols[number]} and 00 / 100"
                    }
            )
        }

        verifySpellIntegers(
                testSample {
                    integer = 100 * 1000 * 1000L
                    expected = "One Hundred Million and 00 / 100"
                },
                testSample {
                    integer = 210 * 1000 * 1000L
                    expected = "Two Hundred Ten Million and 00 / 100"
                },
                testSample {
                    integer = 210 * 1000 * 1000L + 1000 + 100
                    expected = "Two Hundred Ten Million One Thousand One Hundred and 00 / 100"
                }
        )
    }

    @Test
    fun `EnglishNumberSpeller should spell billions correctly`() {
        val symbols = listOf("", "One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine")

        for (number in 1..9) {
            verifySpellIntegers(
                    testSample {
                        integer = number * 1000 * 1000L * 1000L
                        expected = "${symbols[number]} Billion and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000 * 1000L * 1000L + 1000 * 1000L + 1000L
                        expected = "${symbols[number]} Billion One Million One Thousand and 00 / 100"
                    },testSample {
                        integer = number * 1000 * 1000L * 1000L + 1000 * 1000L + 1000L + 100L
                        expected = "${symbols[number]} Billion One Million One Thousand One Hundred and 00 / 100"
                    },
                    testSample {
                        integer = number * 1000 * 1000L * 1000L + 1000 * 1000L + 1000L + 100L + 10L
                        expected = "${symbols[number]} Billion One Million One Thousand One Hundred Ten and 00 / 100"
                    }
            )
        }

        verifySpellIntegers(
                testSample {
                    integer = 100 * 1000 * 1000 * 1000L + 1
                    expected = "One Hundred Billion One and 00 / 100"
                },
                testSample {
                    integer = 100 * 1000 * 1000 * 1000L
                    expected = "One Hundred Billion and 00 / 100"
                },
                testSample {
                    integer = 210 * 1000 * 1000 * 1000L
                    expected = "Two Hundred Ten Billion and 00 / 100"
                },
                testSample {
                    integer = 213 * 1000 * 1000 * 1000L
                    expected = "Two Hundred Thirteen Billion and 00 / 100"
                },
                testSample {
                    integer = 230 * 1000 * 1000 * 1000L
                    expected = "Two Hundred Thirty Billion and 00 / 100"
                },
                testSample {
                    integer = 210 * 1000 * 1000 * 1000L + 1000 * 1000 + 1000 + 100
                    expected = "Two Hundred Ten Billion One Million One Thousand One Hundred and 00 / 100"
                },
                testSample {
                    integer = 210 * 1000 * 1000 * 1000L + 211 * 1000 * 1000 + 1000 + 100
                    expected = "Two Hundred Ten Billion Two Hundred Eleven Million One Thousand One Hundred and 00 / 100"
                }
        )
    }

    @Test
    fun `englishNumberSpeller should spell decimal correctly`() {
        verifySpellIntegers(
                testSample {
                    integer = 0L
                    decimal = 0.1F
                    expected = "Zero and 10 / 100"
                },
                testSample {
                    integer = 1L
                    decimal = 0.1F
                    expected = "One and 10 / 100"
                },
                testSample {
                    integer = 1L
                    decimal = 0.15F
                    expected = "One and 15 / 100"
                },
                testSample {
                    integer = 1L
                    decimal = 0.155F
                    expected = "One and 16 / 100"
                }
        )
    }
}
