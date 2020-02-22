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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import arrow.core.Either
import arrow.core.right
import com.monkeyapp.numbers.translators.SpellerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executors

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var lifecycleOwner:LifecycleOwner

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setup() {
        val lifecycle = LifecycleRegistry(lifecycleOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        doReturn(lifecycle).`when`(lifecycleOwner).lifecycle

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun shutdown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `MainViewModel translates 100000 correctly`() {
        // ----- Given -----
        @Suppress("UNCHECKED_CAST")
        val mockNumberWordsTextObserver = mock(Observer::class.java) as Observer<Either<SpellerError, String>>

        @Suppress("UNCHECKED_CAST")
        val mockNumberTextObserver = mock(Observer::class.java) as Observer<String>

        val coroutineWorkerContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        // ----- When -----
        val viewModel = MainViewModel(coroutineWorkerContext)
        viewModel.numberWordsText.observe(lifecycleOwner, mockNumberWordsTextObserver)
        viewModel.formattedNumberText.observe(lifecycleOwner, mockNumberTextObserver)

        runBlocking {
            "100000".forEach { digit ->
                viewModel.append(digit)
            }
        }

        // ----- Then -----
        verify(mockNumberWordsTextObserver, timeout(300).times(1)).onChanged(eq("One Hundred Thousand and 00 / 100".right()))
        verify(mockNumberTextObserver, timeout(300).times(1)).onChanged(eq("100,000"))
    }

    @Test
    fun `MainViewModel translates zero-point correctly`() {
        // ----- Given -----
        @Suppress("UNCHECKED_CAST")
        val mockNumberWordsTextObserver = mock(Observer::class.java) as Observer<Either<SpellerError, String>>

        @Suppress("UNCHECKED_CAST")
        val mockNumberTextObserver = mock(Observer::class.java) as Observer<String>

        val coroutineWorkerContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        // ----- When -----
        val viewModel = MainViewModel(coroutineWorkerContext)
        viewModel.numberWordsText.observe(lifecycleOwner, mockNumberWordsTextObserver)
        viewModel.formattedNumberText.observe(lifecycleOwner, mockNumberTextObserver)

        runBlocking {
            "0.".forEach { digit ->
                viewModel.append(digit)
            }
        }

        // ----- Then -----
        verify(mockNumberWordsTextObserver, timeout(300).times(1)).onChanged(eq("Zero and 00 / 100".right()))
        verify(mockNumberTextObserver, timeout(300).times(1)).onChanged(eq("0."))
    }

    @Test
    fun `MainViewModel translates point-zero correctly`() {
        // ----- Given -----
        @Suppress("UNCHECKED_CAST")
        val mockNumberWordsTextObserver = mock(Observer::class.java) as Observer<Either<SpellerError, String>>

        @Suppress("UNCHECKED_CAST")
        val mockNumberTextObserver = mock(Observer::class.java) as Observer<String>

        val coroutineWorkerContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        // ----- When -----
        val viewModel = MainViewModel(coroutineWorkerContext)
        viewModel.numberWordsText.observe(lifecycleOwner, mockNumberWordsTextObserver)
        viewModel.formattedNumberText.observe(lifecycleOwner, mockNumberTextObserver)

        runBlocking {
            ".0".forEach { digit ->
                viewModel.append(digit)
            }
        }

        // ----- Then -----
        verify(mockNumberWordsTextObserver, timeout(300).times(1)).onChanged(eq("Zero and 00 / 100".right()))
        verify(mockNumberTextObserver, timeout(300).times(1)).onChanged(eq("0.0"))
    }

    @Test
    fun `MainViewModel translates point-one correctly`() {
        // ----- Given -----
        @Suppress("UNCHECKED_CAST")
        val mockNumberWordsTextObserver = mock(Observer::class.java) as Observer<Either<SpellerError, String>>

        @Suppress("UNCHECKED_CAST")
        val mockNumberTextObserver = mock(Observer::class.java) as Observer<String>

        val coroutineWorkerContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        // ----- When -----
        val viewModel = MainViewModel(coroutineWorkerContext)
        viewModel.numberWordsText.observe(lifecycleOwner, mockNumberWordsTextObserver)
        viewModel.formattedNumberText.observe(lifecycleOwner, mockNumberTextObserver)

        runBlocking {
            ".1".forEach { digit ->
                viewModel.append(digit)
            }
        }

        // ----- Then -----
        verify(mockNumberWordsTextObserver, timeout(300).times(1)).onChanged(eq("Zero and 10 / 100".right()))
        verify(mockNumberTextObserver, timeout(300).times(1)).onChanged(eq("0.1"))
    }
}