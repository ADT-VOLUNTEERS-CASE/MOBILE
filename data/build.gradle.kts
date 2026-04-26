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

tasks.test {
    useJUnitPlatform()

    val allPaths = rootProject.subprojects.flatMap { proj ->
        listOf(
            proj.layout.buildDirectory.dir("classes/kotlin/main/data")
                .map { it.asFile.absolutePath }.orNull,
            proj.layout.buildDirectory.dir("classes/kotlin/test/data")
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

dependencies{
    implementation(project(":domain"))
    implementation(project(":core"))

    implementation(libs.retrofit)
    implementation(libs.converter.scalars)
    implementation(libs.converter.serialization)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(testFixtures(project(":core")))

    testImplementation(libs.mockk)
}
