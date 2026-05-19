plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.pulse.music.player"
    compileSdk = 34
    defaultConfig { minSdk = 26 }
}

dependencies {
    implementation(project(":common-util"))
    implementation(project(":core-database"))
    implementation(project(":core-mediastore"))
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
}