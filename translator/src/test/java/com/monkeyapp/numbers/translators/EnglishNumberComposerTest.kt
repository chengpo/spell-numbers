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

import com.monkeyapp.numbers.testhelpers.shouldEqual
import com.monkeyapp.numbers.testhelpers.shouldLessThan
import org.junit.Test

class EnglishNumberComposerTest {

    @Test
    fun `englishNumberComposer should append digit correctly`() {
        class TestSample(var number: String = "", var numberText: String = "", var wholeNumber: Long = 0, var fraction: Float = 0.0F)

        fun testSample(action: TestSample.() -> Unit) = TestSample().apply(action)

        fun verifySpeller(vararg samples: TestSample) {
            samples.forEach { sample ->
                val englishNumberComposer = EnglishNumberComposer()

                sample.number.forEach { digit ->
                    englishNumberComposer.append(digit)
                }

                englishNumberComposer.observe { numberText, wholeNumber, fraction ->
                    numberText shouldEqual sample.numberText
                    wholeNumber shouldEqual sample.wholeNumber
                    fraction shouldEqual sample.fraction
                }
            }
        }

        verifySpeller(
                testSample {
                    number = "1000"
                    numberText = "1,000"
                    wholeNumber = 1000
                    fraction = 0F
                },
                testSample {
                    number = "1000.00"
                    numberText = "1,000.00"
                    wholeNumber = 1000
                    fraction = 0F
                },
                testSample {
                    number = "1000.1"
                    numberText = "1,000.1"
                    wholeNumber = 1000
                    fraction = 0.1F
                },
                testSample {
                    number = "1000.10"
                    numberText = "1,000.10"
                    wholeNumber = 1000
                    fraction = 0.1F
                },
                testSample {
                    number = "1000.101"
                    numberText = "1,000.101"
                    wholeNumber = 1000
                    fraction = 0.101F
                },
                testSample {
                    number = "1000.106"
                    numberText = "1,000.106"
                    wholeNumber = 1000
                    fraction = 0.106F
                },
                testSample {
                    number = "1000.1001"
                    numberText = "1,000.100"
                    wholeNumber = 1000
                    fraction = 0.10F
                },
                testSample {
                    number = "1000.1016"
                    numberText = "1,000.101"
                    wholeNumber = 1000
                    fraction = 0.101F
                },
                testSample {
                    number = "1000000.1016"
                    numberText = "1,000,000.101"
                    wholeNumber = 1000000
                    fraction = 0.101F
                }
        )
    }

    @Test
    fun `EnglishNumberComposer should append and backspace correctly`() {
        class Expected(var numberText: String = "", var wholeNumber: Long = 0, var fraction: Float = 0.0F)
        fun expected(action: Expected.() -> Unit) =Expected().apply(action)

        class TestSample(var number: String = "", var expected: List<Expected> = emptyList())
        fun testSample(action: TestSample.() -> Unit) = TestSample().apply(action)

        fun verifySpeller(vararg samples: TestSample) {
            samples.forEach { sample ->
                val englishNumberComposer = EnglishNumberComposer()

                var expectedCount = 0

                englishNumberComposer.observe {numberText, wholeNumber, fraction ->
                    expectedCount shouldLessThan sample.expected.size

                    val expected = sample.expected[expectedCount++]

                    numberText shouldEqual expected.numberText
                    wholeNumber shouldEqual expected.wholeNumber
                    fraction shouldEqual expected.fraction
                }

                // append digits
                sample.number.forEach { digit ->
                    englishNumberComposer.append(digit)
                }

                // backspace digits
                var count = sample.number.length
                while (count-- > 0) {
                    englishNumberComposer.backspace()
                }
            }
        }

        verifySpeller(
                testSample {
                    number = "10.00"
                    expected = listOf(
                            expected {
                                numberText = ""
                                wholeNumber = 0
                                fraction = 0.0F
                            },
                            expected {
                              numberText = "1"
                              wholeNumber = 1
                              fraction = 0.0F
                             },
                            expected {
                                numberText = "10"
                                wholeNumber = 10
                                fraction = 0.0F
                            },
                            expected {
                                numberText = "10."
                                wholeNumber = 10
                                fraction = 0.0F
                            },
                            expected {
                                numberText = "10.0"
                                wholeNumber = 10
                                fraction = 0.0F
                            },
                            expected {
                                numberText = "10.00"
                                wholeNumber = 10
                                fraction = 0.0F
                            },
                            expected {
                                numberText = "10.0"
                                wholeNumber = 10
                                fraction = 0.0F
                            },
                            expected {
                                numberText = "10"
                                wholeNumber = 10
                                fraction = 0.0F
                            },
                            expected {
                                numberText = "1"
                                wholeNumber = 1
                                fraction = 0.0F
                            },
                            expected {
                                numberText = ""
                                wholeNumber = 0
                                fraction = 0.0F
                            },
                            expected {
                                numberText = ""
                                wholeNumber = 0
                                fraction = 0.0F
                            }
                    )
                }
        )
    }

    @Test
    fun `EnglishNumberComposer should reset state correctly`() {
        val englishNumberComposer = EnglishNumberComposer()
        "1000".forEach { digit ->
            englishNumberComposer.append(digit)
        }

        var callbackCount = 0

        englishNumberComposer.observe { numberText, wholeNumber, fraction ->
            if (callbackCount++ == 0) {
                numberText shouldEqual "1,000"
                wholeNumber shouldEqual 1000
                fraction shouldEqual 0.0F
            } else {
                numberText shouldEqual ""
                wholeNumber shouldEqual  0
                fraction shouldEqual 0.0F
            }
        }

        englishNumberComposer.reset()
    }

    @Test
    fun `EnglishNumberComposer should ignore duplicated dot`() {
        val englishNumberComposer = EnglishNumberComposer()
        "1000..".forEach { digit ->
            englishNumberComposer.append(digit)
        }

        englishNumberComposer.observe { numberText, wholeNumber, fraction ->
            numberText shouldEqual "1,000."
            wholeNumber shouldEqual 1000
            fraction shouldEqual 0.0F
        }
    }
}
