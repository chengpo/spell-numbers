plugins {
    id("com.android.application")

    kotlin("android")
    kotlin("kapt")

    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    buildToolsVersion = Config.Versions.buildTool
    compileSdk = Config.Android.compileSdk

    defaultConfig {
        applicationId = "com.monkeyapp.numbers"
        versionCode = 45
        versionName = "1.0.22.202108020128"

        minSdk = Config.Android.minSdk
        targetSdk = Config.Android.targetSdk
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders.putAll(mapOf("is_ocr_supported" to true))

        resourceConfigurations.addAll(listOf("en", "nodpi"))
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
            isShrinkResources = true
            isMinifyEnabled = true
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
        checkOnly += setOf("NewApi", "HandlerLeak")
        abortOnError = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Config.Versions.compose
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":appHelper"))
    implementation(project(":translator"))
    implementation(project(":ocr"))

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.annotation:annotation:1.2.0")
    implementation("androidx.core:core:1.6.0")
    implementation("androidx.core:core-ktx:1.6.0")
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
    implementation("com.google.firebase:firebase-analytics-ktx:18.0.2")

    // navigation
    implementation(Config.Libs.navigationFragment)
    implementation(Config.Libs.navigationUI)
    //implementation("androidx.navigation:navigation-compose:2.4.0-alpha04")

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
    implementation("androidx.compose.ui:ui:${Config.Versions.compose}")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:${Config.Versions.compose}")

    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:${Config.Versions.compose}")
    // Material Design
    implementation("androidx.compose.material:material:${Config.Versions.compose}")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:${Config.Versions.compose}")
    implementation("androidx.compose.material:material-icons-extended:${Config.Versions.compose}")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:${Config.Versions.compose}")
    implementation("androidx.compose.runtime:runtime-rxjava2:${Config.Versions.compose}")

    implementation("com.google.accompanist:accompanist-appcompat-theme:0.15.0")
    implementation( "androidx.browser:browser:1.3.0")

    // This dependency is downloaded from the Google’s Maven repository.
    // So, make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:core:1.10.0")

    // For Kotlin users also import the Kotlin extensions library for Play Core:
    implementation("com.google.android.play:core-ktx:1.8.1")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Config.Versions.compose}")

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
