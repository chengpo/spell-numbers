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

import android.app.Application
import androidx.lifecycle.*
import com.monkeyapp.numbers.translators.LargeNumberException
import com.monkeyapp.numbers.translators.TranslatorFactory
import com.monkeyapp.numbers.translators.TranslatorFactory.Translator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel() {
    @field:[Inject Named("coroutineMainContext")]
    lateinit var coroutineContextMain: CoroutineContext

    @field:[Inject Named("coroutineWorkerContext")]
    lateinit var coroutineContextWorker: ExecutorCoroutineDispatcher

    private val translator: Translator = TranslatorFactory.englishTranslator

    private val _numberWords = MutableLiveData<NumberWords>()
    private val _error = MutableLiveData<Throwable>()

    val numberWords: LiveData<NumberWords>
        get() = _numberWords

    val error: LiveData<Throwable>
        get() = _error

    init {
        Injector.getInstance().inject(this)

        translator.observe { numberText: String, wordsText: String ->
            viewModelScope.launch(coroutineContextMain) {
                _numberWords.value = NumberWords(numberText, wordsText)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContextWorker.close()
    }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        viewModelScope.launch(coroutineContextMain) {
            _error.value = exception
        }
    }

    fun append(digit: Char) {
        viewModelScope.launch(coroutineContextWorker + exceptionHandler) {
            try {
                translator.append(digit)
            } catch (e: LargeNumberException) {
                translator.backspace()
                throw e
            }
        }
    }

    fun backspace() {
        viewModelScope.launch(coroutineContextWorker + exceptionHandler) {
            translator.backspace()
        }
    }

    fun reset() {
        viewModelScope.launch(coroutineContextWorker) {
            translator.reset()
        }
    }
}

data class NumberWords(val numberText: String, val wordsText: String)
