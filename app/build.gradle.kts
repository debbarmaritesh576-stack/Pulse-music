plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.pulse.music"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pulse.music"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.5" }
}

dependencies {
    implementation(project(":core-player"))
    implementation(project(":core-database"))
    implementation(project(":core-mediastore"))
    implementation(project(":core-equalizer"))
    implementation(project(":feature-home"))
    implementation(project(":feature-player"))
    implementation(project(":feature-library"))
    implementation(project(":feature-search"))
    implementation(project(":feature-settings"))
    implementation(project(":feature-lyrics"))
    implementation(project(":feature-widget"))
    implementation(project(":common-ui"))
    implementation(project(":common-util"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.navigation.compose)
}