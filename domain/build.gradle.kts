plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)

    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

dependencies {
    implementation(project("::core"))

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)
}