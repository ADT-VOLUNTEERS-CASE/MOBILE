plugins {
    id("java-library")

    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet.ksp)

    implementation(libs.kotlinx.serialization.json)
}