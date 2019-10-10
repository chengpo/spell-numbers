package com.monkeyapp.numbers

import androidx.annotation.VisibleForTesting
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class Injector(mainComponent: MainComponent) : MainComponent by mainComponent {
    companion object {
        @VisibleForTesting
        var _instance: Injector? = null

        val instance: Injector
            get() {
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