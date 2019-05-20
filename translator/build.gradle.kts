plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    buildToolsVersion("28.0.3")
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(17)
        targetSdkVersion(28)

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.31")
    implementation("io.arrow-kt:arrow-core:0.7.3")

    testImplementation(project(":testHelper"))
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.22.0")
    testImplementation("androidx.arch.core:core-testing:2.0.0")
    testImplementation("org.hamcrest:hamcrest-junit:2.0.0.0")
}
repositories {
    mavenCentral()
}
