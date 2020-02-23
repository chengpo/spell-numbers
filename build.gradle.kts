// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.*

buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Config.Plugins.android)
        classpath(Config.Plugins.kotlin)
        classpath(Config.Plugins.navigation)
        classpath(Config.Plugins.googleService)
        classpath(Config.Plugins.detekt)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version(Config.Versions.detektPlugin)
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        buildUponDefaultConfig = true
        config = files("${rootDir}/detekt-config.yml")
    }

    tasks.withType(Test::class) {
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = setOf(
                    TestLogEvent.SKIPPED,
                    TestLogEvent.PASSED,
                    TestLogEvent.FAILED,
                    TestLogEvent.STANDARD_OUT,
                    TestLogEvent.STANDARD_ERROR)
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
