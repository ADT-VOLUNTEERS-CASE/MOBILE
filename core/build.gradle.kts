plugins {
    id("java-library")
    id("java-test-fixtures")

    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
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
}

dependencies {
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet.ksp)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)

    testFixturesApi(libs.junit.jupiter)
    testFixturesApi(libs.archunit.junit5)
    testFixturesRuntimeOnly(libs.junit.jupiter.engine)
    testFixturesApi(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()

    testClassesDirs += sourceSets["testFixtures"].output.classesDirs
    classpath += sourceSets["testFixtures"].runtimeClasspath

    val allPaths = rootProject.subprojects.flatMap { proj ->
        listOf(
            proj.layout.buildDirectory.dir("classes/kotlin/main")
                .map { it.asFile.absolutePath }.orNull,
            proj.layout.buildDirectory.dir("classes/kotlin/test")
                .map { it.asFile.absolutePath }.orNull
        )
    }.filterNotNull().filter { File(it).exists() }

    systemProperty("project.class.dirs", allPaths.joinToString(","))

    maxHeapSize = "2g"

    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = false
    }
}