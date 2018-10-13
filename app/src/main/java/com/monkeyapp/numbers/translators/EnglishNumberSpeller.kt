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


class EnglishNumberSpeller: NumberSpeller() {
    private val symbols = listOf(
           "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
           "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen",
           "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

    override fun spellNumber(wholeNumber: Long, fraction: Float) =
            "${spellWholeNumber(wholeNumber)} and ${spellFraction(fraction)}"

    private fun spellFraction(fraction: Float) =
            String.format("%02d / 100", Math.round(fraction * 100))
    
    private fun spellWholeNumber(wholeNumber: Long): Node =
            when (wholeNumber) {
                in 0 until 20 -> Node(symbols[wholeNumber.toInt()])
                
                in 20 until 100 -> {
                    val index = wholeNumber / 10 - 2

                    Node(symbols[20 + index.toInt()]).apply {
                        if (wholeNumber % 10 > 0) {
                            append(spellWholeNumber(wholeNumber % 10))
                        }
                    }
                }

                in 100 until 1000 -> {
                    val index = wholeNumber / 100

                    Node(symbols[index.toInt()]).apply {
                        val tail = append("Hundred")
                        if (wholeNumber % 100 > 0) {
                            tail.append(spellWholeNumber(wholeNumber % 100))
                        }
                    }
                }

                in 1000 until 1000 * 1000 ->
                    spellWholeNumber(wholeNumber / 1000).apply {
                        val tail = append("Thousand")
                        if (wholeNumber % 1000 > 0) {
                            tail.append(spellWholeNumber(wholeNumber % 1000))
                        }
                    }

                in 1000 * 1000 until 1000 * 1000 * 1000 ->
                    spellWholeNumber(wholeNumber / (1000 * 1000)).apply {
                        val tail = append("Million")
                        if (wholeNumber % (1000 * 1000) > 0) {
                            tail.append(spellWholeNumber(wholeNumber % (1000 * 1000)))
                        }
                    }

                in 1000 * 1000 * 1000 until 1000 * 1000 * 1000 * 1000L ->
                    spellWholeNumber(wholeNumber / (1000 * 1000 * 1000)).apply {
                        val tail = append("Billion")
                        if (wholeNumber % (1000 * 1000 * 1000) > 0) {
                            tail.append(spellWholeNumber(wholeNumber % (1000 * 1000 * 1000)))
                        }
                    }

                else -> throw LargeNumberException()
            }

     private class Node(var symbol: String, var next: Node? = null) {
         fun append(nextSymbol: String) = append(Node(nextSymbol))

         fun append(nextNode: Node): Node {
             var node:Node? = this
             while (node?.next != null) {
                 node = node.next
             }

             node?.next = nextNode
             return nextNode
         }

         override fun toString(): String {
             val sb = StringBuffer()
             var node: Node? = this
             do {
                 sb.append(node?.symbol)
                 node = node?.next

                 if (node != null) {
                     sb.append(" ")
                 }
             } while (node != null)

             return sb.toString()
         }
     }
}