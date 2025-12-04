plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")  // 添加这行
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.qm_app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.qm_app"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("me.onebone:toolbar-compose:2.3.5")
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")  // 注解处理器
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    implementation("androidx.navigation:navigation-compose:2.9.6")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}