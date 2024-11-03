plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.monkeyapp.numbers.translator"
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
            isMinifyEnabled = true
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
    implementation(Config.Libs.kotlin)
    implementation(Config.Libs.arrow)

    testImplementation(project(":testHelper"))
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:3.0.0")
    testImplementation("androidx.arch.core:core-testing:2.0.0")
    testImplementation("org.hamcrest:hamcrest-junit:2.0.0.0")
}
