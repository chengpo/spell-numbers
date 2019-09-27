package com.monkeyapp.numbers

import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
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
    @Provides @Named("coroutineMainContext")
    open fun provideMainContext(): CoroutineContext = Dispatchers.Main

    @Provides @Named("coroutineWorkerContext")
    open fun provideWorkerContext(): ExecutorCoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
}

@Component(modules = [CoroutineContextModule::class])
interface MainComponent {
    fun inject(mainViewModel: MainViewModel)
}