import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
}