plugins {
    id("com.android.application")

    kotlin("android")
    kotlin("kapt")

    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.monkeyapp.numbers"
    compileSdk = Config.Android.compileSdk

    defaultConfig {
        applicationId = "com.monkeyapp.numbers"
        versionCode = 50
        versionName = "1.0.25.202411161349"

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

        buildConfig = true
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
            isPseudoLocalesEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")

            buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
        }
    }

    lint {
        checkOnly += setOf("NewApi", "HandlerLeak")
        abortOnError = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Config.Versions.composeCompiler
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
    implementation("com.google.android.gms:play-services-ads:23.5.0")

    // viewModel and liveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.3.0")

    implementation("com.google.firebase:firebase-analytics:21.3.0")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-bom:30.1.0")

    // navigation
    implementation(Config.Libs.navigationFragment)
    implementation(Config.Libs.navigationUI)

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
    implementation("com.google.android.play:asset-delivery:2.2.2")

    // For Kotlin users also import the Kotlin extensions library for Play Asset Delivery:
    implementation("com.google.android.play:asset-delivery-ktx:2.2.2")

    // This dependency is downloaded from the Google’s Maven repository.
    // Make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:feature-delivery:2.1.0")

    // For Kotlin users, also import the Kotlin extensions library for Play Feature Delivery:
    implementation("com.google.android.play:feature-delivery-ktx:2.1.0")

    // This dependency is downloaded from the Google’s Maven repository.
    // Make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:review:2.0.2")

    // For Kotlin users, also import the Kotlin extensions library for Play In-App Review:
    implementation("com.google.android.play:review-ktx:2.0.2")

    // This dependency is downloaded from the Google’s Maven repository.
    // Make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:app-update:2.1.0")

    // For Kotlin users, also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")


    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Config.Versions.compose}")


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
