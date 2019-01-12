/*
MIT License

Copyright (c) 2017 - 2019 Po Cheng

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
import arrow.core.Try
import arrow.core.getOrElse
import org.junit.Test

class EnglishTranslatorTest {
    @Test
    fun `EnglishTranslator should translate number correctly`() {
        val translator = TranslatorFactory.getEnglishTranslator()
        "1000".forEach { digit ->
            translator.append(digit)
        }

        translator.observe { numberText, wordsText ->
            numberText shouldEqual "1,000"
            wordsText shouldEqual "One Thousand and 00 / 100"
        }
    }

    @Test
    fun `EnglishTranslator returns blank string for empty input`() {
        val translator = TranslatorFactory.getEnglishTranslator()
        translator.observe { numberText, wordsText ->
            numberText shouldEqual ""
            wordsText shouldEqual ""
        }
    }

    @Test
    fun `EnglishTranslator throws exception for too large number`() {
        val translator = TranslatorFactory.getEnglishTranslator()
        var getException = false
        val largeNumber = (Math.pow(1000.0, 5.0) * 10).toLong()

        Try {
            largeNumber.toString().forEach { digit ->
                translator.append(digit)
            }

            translator.observe { _, _ ->
                getException = false
            }

        }.getOrElse {
            getException = true
        }

        getException shouldEqual true
    }
}