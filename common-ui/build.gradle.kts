plugins {  
    id("com.android.library")  
    id("org.jetbrains.kotlin.android")  
}  
  
android {  
    namespace = "com.pulse.music.ui"  
    compileSdk = 34  
  
    defaultConfig {  
        minSdk = 26  
    }  
  
    buildFeatures {  
        compose = true  
    }  
  
    composeOptions {  
        kotlinCompilerExtensionVersion = "1.5.5"  
    }  
}  
  
dependencies {  
    implementation(platform(libs.compose.bom))  
    implementation(libs.compose.ui)  
    implementation(libs.compose.material3)  
}