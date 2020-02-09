/*
MIT License

Copyright (c) 2017 - 2020 Po Cheng

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

import arrow.core.*
import java.lang.StringBuilder
import kotlin.math.pow
import kotlin.math.roundToLong

enum class SpellerError {
    NUMBER_IS_TOO_LARGE
}

fun spellNumberInEnglish(numberText: String) = EnglishNumber(numberText).spell()

data class Number(private val numberText: String) {
    val integer: Eval<Long> = Eval.later {
        val wholeNumberText = numberText.substringBefore(delimiter = '.')
        wholeNumberText.fold(0L) { accumulator, digit ->
            (accumulator * 10) + (digit - '0')
        }
    }

    val fraction: Eval<Float> = Eval.later {
        val fractionText = numberText.substringAfter('.', missingDelimiterValue = "")
        fractionText.foldRightIndexed(0.0F) { index, digit, accumulator ->
            accumulator + (digit - '0') * (0.1.pow(index + 1)).toFloat()
        }
    }
}

class EnglishNumber(private val numberText: String) {
    private val symbols = listOf(
            "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen",
            "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

    private val radixSymbols = listOf("Hundred", "Thousand", "Million", "Billion", "Trillion")

    fun spell(): Either<SpellerError, String> {
        if (numberText.isEmpty()) {
            return "".right()
        }

        val number = Number(numberText)

        val decimalText = number.fraction.map(::spellDecimal)
        val integerText = number.integer.map(::spellInteger)

        return integerText
                .value()
                .map { "$it and ${decimalText.value()}" }
    }

    private fun spellDecimal(decimal: Float): String =
            String.format("%02d / 100", (decimal * 100).roundToLong())

    private fun spellInteger(wholeNumber: Long): Either<SpellerError, String> {
        if (wholeNumber == 0L) {
            return symbols[0].right()
        }

        val words = StringBuilder()
        var number = wholeNumber
        var exponent = 0

        // find the maximum radix
        while (number >= 1000) {
            number /= 1000

            if (++exponent >= radixSymbols.size) {
                return SpellerError.NUMBER_IS_TOO_LARGE.left()
            }
        }

        number = wholeNumber

        while (exponent >= 0) {
            val base = 1000.0.pow(exponent.toDouble()).toLong()
            val hundred = number / base
            if (hundred > 0) {
                spellHundreds(hundred, words)
                if (exponent > 0) {
                    words.append("${radixSymbols[exponent]} ")
                }
            }

            number %= base
            exponent--
        }

        return words.toString().trimEnd().right()
    }

    private fun spellHundreds(num: Long, words: StringBuilder) {
        val hundred = (num / 100).toInt()
        if (hundred > 0) {
            words.append("${symbols[hundred]} ${radixSymbols[0]} ")
        }

        var remainders = (num % 100).toInt()
        when (remainders) {
            in 1 until 20 -> words.append("${symbols[remainders]} ")
            in 20 until 100 -> {
                val index = 20 + remainders / 10 - 2
                words.append("${symbols[index]} ")

                remainders %= 10
                if (remainders > 0) {
                    words.append("${symbols[remainders]} ")
                }
            }
        }
    }
}