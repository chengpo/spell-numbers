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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var lifecycleOwner:LifecycleOwner

    @Test
    fun mainViewModel_should_return_translated_text_correctly() {
        // ----- Given -----
        val lifecycle = LifecycleRegistry(lifecycleOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        doReturn(lifecycle).`when`(lifecycleOwner).lifecycle

        val mockObserver = mock(Observer::class.java) as Observer<NumberWords>

        // ----- When -----
        val viewModel = MainViewModel()
        viewModel.numberWords.observe(lifecycleOwner, mockObserver)

        runBlocking {
            "100000".forEach { digit ->
                viewModel.append(digit)
            }
        }

        // ----- Then -----
        val numberWords = NumberWords(numberText = "100,000",
                                      wordsText = "One Hundred Thousand and 00 / 100")
        verify(mockObserver, timeout(300).times(1)).onChanged(eq(numberWords))
    }
}