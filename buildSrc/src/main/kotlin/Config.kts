object Versions {
    const val buildTool = "28.0.3"
    const val androidPlugin = "3.4.1"
    const val kotlin = "1.3.31"
    const val arrow = "0.7.3"
    const val appcompat = "1.0.2"
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
}

object Android {
    const val compileSdk = 28
    const val minSdk = 17
    const val targetSdk = 28
}


