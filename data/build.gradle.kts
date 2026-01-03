plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)

    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
dependencies{
    implementation(project("::domain"))
    implementation(project("::core"))

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)
}
