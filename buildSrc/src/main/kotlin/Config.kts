import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.*

object Versions {
    const val buildTool = "30.0.3"
    const val androidPlugin = "8.0.1"
    const val detektPlugin = "1.0.0-RC15"
    const val googleServicesPlugin = "4.3.5"
    const val kotlin = "1.8.20"
    const val arrow = "0.10.1"
    const val appcompat = "1.3.0-rc01"
    const val coroutines = "1.4.2"
    const val navigation = "2.3.5"
    const val compose = "1.0.1"
}

object Plugins {
    const val android = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val navigation = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val googleService = "com.google.gms:google-services:${Versions.googleServicesPlugin}"
    const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detektPlugin}"
}

object Libs {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val appCompatResources = "androidx.appcompat:appcompat-resources:${Versions.appcompat}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val arrow = "io.arrow-kt:arrow-core:${Versions.arrow}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
}

object Android {
    const val compileSdk = 30
    const val minSdk = 21
    const val targetSdk = 30
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



