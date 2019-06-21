import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.*

object Versions {
    const val buildTool = "28.0.3"
    const val androidPlugin = "3.4.1"
    const val kotlin = "1.3.31"
    const val arrow = "0.7.3"
    const val appcompat = "1.0.2"
    const val coroutines = "1.3.0-M1"
    const val anko = "0.10.8"
}

object Plugins {
    const val android = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val navigation = "android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0"
    const val googleService = "com.google.gms:google-services:4.2.0"
}

object Libs {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val arrow = "io.arrow-kt:arrow-core:${Versions.arrow}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val anko = "org.jetbrains.anko:anko-commons:${Versions.anko}"
}

object Android {
    const val compileSdk = 28
    const val minSdk = 17
    const val targetSdk = 28
}

data class KeyStore(
        val keyAlias: String,
        val keyPassword: String,
        val storeFile: File,
        val storePassword: String
) {
    companion object {
        fun load(project: Project): KeyStore {
            val keystorePropertiesFile = project.file("keystore.properties")
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))

            return KeyStore(
                    keyAlias = keystoreProperties["keyAlias"] as String,
                    keyPassword = keystoreProperties["keyPassword"] as String,
                    storeFile = project.file(keystoreProperties["storeFile"] as String),
                    storePassword = keystoreProperties["storePassword"] as String
            )
        }
    }
}



