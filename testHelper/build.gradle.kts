plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.monkeyapp.numbers.testhelper"
    compileSdk = Config.Android.compileSdk

    defaultConfig {
        minSdk = Config.Android.minSdk

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    lint {
        targetSdk = Config.Android.targetSdk
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

    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {
    compileOnly(Config.Libs.kotlin)

    compileOnly("junit:junit:4.12")
    compileOnly("org.hamcrest:hamcrest-junit:2.0.0.0")
}
