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

package com.monkeyapp.numbers

import androidx.lifecycle.*
import arrow.core.Either
import com.monkeyapp.numbers.translators.*
import kotlinx.coroutines.*
import java.util.concurrent.Executors

class MainViewModel(private val coroutineWorkerContext: ExecutorCoroutineDispatcher) : ViewModel() {
    private val numberText = MutableLiveData<String>()

    val formattedNumberText: LiveData<String>
        get() {
            return Transformations.switchMap(numberText) {
                liveData(coroutineWorkerContext) {
                    emit(formatNumber(it, delimiter = ',', delimiterWidth = 3))
                }
            }
        }

    val numberWordsText: LiveData<Either<SpellerError, String>>
        get() {
            return Transformations.switchMap(numberText) {
                liveData(coroutineWorkerContext) {
                    val spelledText = spellNumberInEnglish(it)
                    emit(spelledText)

                    // if encounter error
                    if (spelledText.isLeft()) {
                        withContext(Dispatchers.Main) {
                            backspace()
                        }
                    }
                }
            }
        }

    fun append(digit: Char) {
        numberText.value = appendDigit(numberText.value ?: "", digit)
    }

    fun backspace() {
        numberText.value = deleteDigit(numberText.value ?: "")
    }

    fun reset() {
        numberText.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        coroutineWorkerContext.close()
    }

    private class MainViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(coroutineWorkerContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()) as T
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory
            get() = MainViewModelFactory()
    }
}

