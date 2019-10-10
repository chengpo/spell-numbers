plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
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
    implementation(project(":appHelper"))
    implementation(Config.Libs.appCompat)

    // google vision service
    implementation("com.google.android.gms:play-services-vision:17.0.2") {
        exclude(group = "com.android.support", module = "support-v4")
    }
    // kotlin
    implementation(Config.Libs.kotlin)

    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0-alpha4", {
        exclude(group = "com.android.support", module = "support-annotations")
    })

    testImplementation("junit:junit:4.12")
}
