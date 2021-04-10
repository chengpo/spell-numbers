plugins {
    id("com.android.application")

    kotlin("android")
    kotlin("kapt")

    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    buildToolsVersion(Config.Versions.buildTool)
    compileSdkVersion(Config.Android.compileSdk)

    defaultConfig {
        applicationId = "com.monkeyapp.numbers"
        versionCode = 42
        versionName = "1.0.21.202102250205"

        minSdkVersion(Config.Android.minSdk)
        targetSdkVersion(Config.Android.targetSdk)
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders.putAll(mapOf("is_ocr_supported" to true))

        resConfigs("en", "nodpi")
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }

    signingConfigs {
        create("release") {
            val keystore = Config.KeyStore.load(rootProject)
            logger.lifecycle("keystore = $keystore")

            keyAlias =  keystore.keyAlias
            keyPassword = keystore.keyPassword
            storeFile = keystore.storeFile
            storePassword = keystore.storePassword
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            isPseudoLocalesEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }

    lint {
        checkOnly("NewApi", "HandlerLeak")
        isAbortOnError = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-beta04"
    }

    kotlinOptions {
        useIR = true
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":appHelper"))
    implementation(project(":translator"))
    implementation(project(":ocr"))

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.core:core:1.3.2")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation(Config.Libs.appCompat)
    implementation(Config.Libs.appCompatResources)
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0")

    // admob
    implementation("com.google.android.gms:play-services-ads:19.7.0")

    // viewModel and liveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0")
    //kapt("androidx.lifecycle:lifecycle-compiler:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.3.0")

    implementation("com.google.firebase:firebase-core:18.0.2")
    implementation("com.google.firebase:firebase-analytics:18.0.2")

    // navigation
    implementation(Config.Libs.navigationFragment)
    implementation(Config.Libs.navigationUI)
    implementation("androidx.navigation:navigation-compose:1.0.0-alpha10")

    // optional - Test helpers
    // this library depends on the Kotlin standard library
    androidTestImplementation("android.arch.navigation:navigation-testing:1.0.0-alpha08")

    // kotlin
    implementation(Config.Libs.kotlin)
    implementation(Config.Libs.arrow)

    // coroutines
    implementation(Config.Libs.coroutinesCore)
    implementation(Config.Libs.coroutinesAndroid)

    // compose
    implementation("androidx.compose.compiler:compiler:1.0.0-beta04")
    implementation("androidx.compose.ui:ui:1.0.0-beta01")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.0.0-beta01")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.0.0-beta01")
    // Material Design
    implementation("androidx.compose.material:material:1.0.0-beta01")
    // Material design icons
    implementation( "androidx.compose.material:material-icons-core:1.0.0-beta01")
    implementation("androidx.compose.material:material-icons-extended:1.0.0-beta01")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.3.0-alpha03")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha02")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:1.0.0-beta01")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.0.0-beta01")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0-beta01")

    // dagger2
    // implementation("com.google.dagger:dagger:2.24")
    // kapt("com.google.dagger:dagger-compiler:2.24")

    androidTestImplementation("androidx.arch.core:core-testing:2.0.0")
    androidTestImplementation("androidx.test:runner:1.2.0")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0-alpha4") {
        exclude(group = "com.android.support", module = "support-annotations")
    }

    testImplementation(project(":testHelper"))

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:1.10.19")
    testImplementation("androidx.arch.core:core-testing:2.0.0")
    testImplementation("org.hamcrest:hamcrest-junit:2.0.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.2")
}
