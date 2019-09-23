package com.monkeyapp.numbers

import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


class Injector(private val mainComponent: MainComponent) : MainComponent by mainComponent {
    companion object {
        var _instance: Injector? = null

        fun getInstance(): Injector {
            if (_instance == null) {
                val mainComponent = DaggerMainComponent.builder()
                        .coroutineContextModule(CoroutineContextModule())
                        .build()

                _instance = Injector(mainComponent)
            }

            return _instance!!
        }
    }
}

@Module
open class CoroutineContextModule {
    @Provides
    open fun provideMain(): CoroutineContext = Dispatchers.Main
}

@Component(modules = [CoroutineContextModule::class])
interface MainComponent {
    fun inject(mainViewModel: MainViewModel)
}