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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import arrow.core.Try
import com.monkeyapp.numbers.translators.NumberTranslator
import com.monkeyapp.numbers.translators.TranslatorFactory

class MainViewModel : ViewModel() {
    private val translator: NumberTranslator
    private val viewObjLiveData = MutableLiveData<ViewObject>()

    init {
        // TODO : support other language translators
        translator = TranslatorFactory.getEnglishTranslator { numberText: String, wordsText: String ->
            viewObjLiveData.value = ViewObject(numberText, wordsText)
        }
    }

    fun deleteDigit() = translator.deleteDigit()

    fun appendDigit(digit: Char) = Try { translator.appendDigit(digit) }

    fun appendDigit(digits: CharSequence) {
        digits.forEach {
            appendDigit(it)
        }
    }

    fun resetDigit() = translator.resetDigit()

    fun observe(owner: LifecycleOwner, observer: (viewObj: ViewObject?) -> Unit) {
        viewObjLiveData.observe(owner, Observer { observer(it) })
    }

    data class ViewObject(val numberText: String, val wordsText: String)
}

