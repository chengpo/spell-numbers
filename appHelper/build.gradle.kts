

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    buildToolsVersion(Config.Versions.buildTool)
    compileSdkVersion(Config.Android.compileSdk)

    defaultConfig {
        minSdkVersion(Config.Android.minSdk)
        targetSdkVersion(Config.Android.targetSdk)

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            consumerProguardFiles("proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("com.google.android.material:material:1.0.0")
    implementation(Config.Libs.appCompat)
    implementation(Config.Libs.kotlin)

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.2.1")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0-alpha4", {
        exclude(group = "com.android.support", module = "support-annotations")
    })

    testImplementation("junit:junit:4.12")
}

repositories {
    mavenCentral()
}