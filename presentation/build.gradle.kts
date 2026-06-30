import com.github.takahirom.roborazzi.ExperimentalRoborazziApi

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlinx-serialization")

    id("io.github.takahirom.roborazzi")

    alias(libs.plugins.stability.analyzer)
}
android {
    namespace = "org.adt.presentation"
    compileSdk = 37

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "org.adt.presentation"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.1"

        testInstrumentationRunner = "org.adt.presentation.HiltTestRunner"

        buildConfigField("String", "API_BASE_URL", "\"https://adt.rss14.ru/api/\"")
    }

    signingConfigs {
        create("release") {
            val path = System.getenv("ANDROID_KEYSTORE_PATH")
            val ksPass = System.getenv("ANDROID_KEYSTORE_PASSWORD")
            val alias = System.getenv("ANDROID_KEY_ALIAS")
            val keyPass = System.getenv("ANDROID_KEY_PASSWORD")

            if (!path.isNullOrBlank() && !ksPass.isNullOrBlank()
                && !alias.isNullOrBlank() && !keyPass.isNullOrBlank()
            ) {
                storeFile = file(path)
                storePassword = ksPass
                keyAlias = alias
                keyPassword = keyPass
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig = signingConfigs.getByName("release")
            buildConfigField("String", "API_BASE_URL", "\"https://adt.rss14.ru/api/\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("releaseNoSign") {
            initWith(getByName("release"))

            applicationIdSuffix = ".nosign"
            versionNameSuffix = "-nosign"

            signingConfig = signingConfigs.getByName("debug")

            matchingFallbacks.add("release")
        }

        create("debugMinify") {
            initWith(getByName("debug"))

            // Disable debugging for obfuscated code
            isDebuggable = false

            applicationIdSuffix = ".debugMinify"
            versionNameSuffix = "-debugMinify"

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            matchingFallbacks.add("debug")

            // Enable debug tools
            buildConfigField("Boolean", "DEBUG", "true")
        }
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }

    sourceSets {
        named("releaseNoSign") {
            java.directories.add("src/release/java")
            kotlin.directories.add("src/release/kotlin")
            res.directories.add("src/release/res")
        }

        named("debugMinify") {
            java.directories.add("src/debug/java")
            kotlin.directories.add("src/debug/kotlin")
            res.directories.add("src/debug/res")
        }
    }
}

roborazzi {
    @OptIn(ExperimentalRoborazziApi::class) generateComposePreviewRobolectricTests {
        enable = true
        packages = listOf("org.adt.presentation")
        includePrivatePreviews = true
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-XXLanguage:+ContextParameters")
    }
}

dependencies {
    implementation(project("::domain"))
    implementation(project("::data")) // TODO: MIGRATE TO DOMAIN
    implementation(project("::core"))
    implementation(project("::storage"))
    implementation(libs.androidx.compose.ui.geometry)


    implementation(libs.hilt.android)
    implementation(libs.androidx.core.ktx)

    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.junit)
    testImplementation(libs.roborazzi.compose.preview.scanner.support)
    testImplementation(libs.robolectric)
    testImplementation(libs.android)
    testImplementation(libs.androidx.compose.ui.test.junit4)

    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit.rule)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)

    implementation(libs.dotsindicator)

    implementation(libs.shadowglow)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.compose)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.coil.compose)
}