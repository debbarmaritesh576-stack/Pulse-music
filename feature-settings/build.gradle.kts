plugins {  
    id("com.android.library")  
    id("org.jetbrains.kotlin.android")  
}  
  
android {  
    namespace = "com.pulse.music.settings"  
    compileSdk = 34  
    defaultConfig { minSdk = 26 }  
    buildFeatures { compose = true }  
    composeOptions { kotlinCompilerExtensionVersion = "1.5.5" }  
}  
  
dependencies {  
    implementation(project(":common-ui"))  
    implementation(project(":common-util"))  
    implementation(project(":core-player"))  
    implementation(project(":core-equalizer"))  
    implementation(platform(libs.compose.bom))  
    implementation(libs.compose.ui)  
    implementation(libs.compose.material3)  
    implementation(libs.compose.icons)  
    implementation(libs.hilt.android)  
    implementation(libs.navigation.compose)  
}