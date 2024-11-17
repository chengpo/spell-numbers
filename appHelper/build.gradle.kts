import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.monkeyapp.numbers.apphelpers"
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
    implementation("com.google.android.material:material:1.3.0")
    implementation(Config.Libs.appCompat)
    implementation(Config.Libs.kotlin)
    implementation("androidx.core:core:1.3.2")
    implementation("androidx.core:core-ktx:1.3.2")

    // coroutines
    implementation(Config.Libs.coroutinesCore)
    implementation(Config.Libs.coroutinesAndroid)

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0-alpha4") {
        exclude(group = "com.android.support", module = "support-annotations")
    }

    testImplementation("junit:junit:4.12")
}

repositories {
    mavenCentral()
}