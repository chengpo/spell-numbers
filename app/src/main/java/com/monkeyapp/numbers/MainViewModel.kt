package com.monkeyapp.numbers

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.monkeyapp.numbers.translators.NumberObserver
import com.monkeyapp.numbers.translators.Translator
import com.monkeyapp.numbers.translators.TranslatorFactory


class MainViewModel(private val translator: Translator =
                                TranslatorFactory.getEnglishTranslator()) :
                                ViewModel(), Translator by translator {
    val digitStr = MutableLiveData<String>()
    val numberStr = MutableLiveData<String>()

    init {
        translator.observer = object: NumberObserver {
            override fun onNumberUpdated(digitStr: String, numberStr: String) {
                this@MainViewModel.digitStr.value = digitStr
                this@MainViewModel.numberStr.value = numberStr
            }
        }
    }
}