plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.monkeyapp.numbers.testhelper"
    buildToolsVersion = Config.Versions.buildTool
    compileSdk = Config.Android.compileSdk

    defaultConfig {
        minSdk = Config.Android.minSdk
        targetSdk = Config.Android.targetSdk

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly(Config.Libs.kotlin)

    compileOnly("junit:junit:4.12")
    compileOnly("org.hamcrest:hamcrest-junit:2.0.0.0")
}
