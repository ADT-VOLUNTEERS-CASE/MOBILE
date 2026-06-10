// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false

    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.stability.analyzer) apply false
    alias(libs.plugins.android.library) apply false
}

subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-metadata-jvm") {
                useVersion("2.4.0")
                because("Aligns Hilt 2.59.2 with kotlin-metadata-jvm 2.4.0 requirement")
            }
        }
    }
}
