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

package com.monkeyapp.numbers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monkeyapp.numbers.translators.NumberComposer
import com.monkeyapp.numbers.translators.TranslatorFactory
import com.monkeyapp.numbers.translators.TranslatorFactory.Translator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class MainViewModel(private val translator: Translator = TranslatorFactory.englishTranslator) :
        ViewModel(), NumberComposer {

    private val translatorContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val numberWordsLiveData = MutableLiveData<NumberWords>()
    private val errorLiveData = MutableLiveData<Exception>()

    val numberWords: LiveData<NumberWords>
        get() = numberWordsLiveData

    val error: LiveData<Exception>
        get() = errorLiveData

    init {
        translator.observe { numberText: String, wordsText: String ->
            viewModelScope.launch(Dispatchers.Main) {
                numberWordsLiveData.value = NumberWords(numberText, wordsText)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        translatorContext.close()
    }

    override fun append(digit: Char) {
        viewModelScope.launch(translatorContext) {
            try {
                translator.append(digit)
            } catch (e: Exception) {
                translator.backspace()

                withContext(Dispatchers.Main) {
                    errorLiveData.value = e
                }
            }
        }
    }

    override fun backspace() {
        viewModelScope.launch(translatorContext) {
            try {
                translator.backspace()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorLiveData.value = e
                }
            }
        }
    }

    override fun reset() {
        viewModelScope.launch(translatorContext) {
            translator.reset()
        }
    }
}

data class NumberWords(val numberText: String, val wordsText: String)

