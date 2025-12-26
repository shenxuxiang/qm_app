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

        manifestPlaceholders["appName"] = "阡陌农服"
    }
    flavorDimensions.add("environment")
    // 定义环境维度下的风味
    productFlavors {
        create("dev") {
            // 属于环境维度
            dimension = "environment"
            // 包名后缀
            applicationIdSuffix = ".dev"
            // 版本名后缀
            versionNameSuffix = "-alpha"

            buildConfigField(type = "String", name = "ENVIRONMENT", value = "\"development\"")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"http://60.169.69.3:30062\""
            )
            resValue(type = "string", name = "app_name", value = "阡陌农服dev")
            manifestPlaceholders["appName"] = "阡陌农服dev"
        }

        create("prod") {
            dimension = "environment"
            buildConfigField(type = "String", name = "ENVIRONMENT", value = "\"production\"")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"http://60.169.69.3:30066\""
            )
            resValue(type = "string", name = "app_name", value = "阡陌农服")
        }
    }

    // 配置签名文件
    signingConfigs {
        create("release") {
            storeFile = file("./release-keystore.jks")
            keyAlias = "release-keystore"
            storePassword = "sqal@145680"
            keyPassword = "sqal@145680"
        }
    }

    buildTypes {
        debug {
            // 确保 BuildConfig.DEBUG 在调试模式下为 true
            // 这行配置不是必须的，但显式声明更清晰，有助于确保类被生成
            buildConfigField(type = "Boolean", name = "DEBUG", value = "true")
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 2. 为 release 构建类型也配置一个 DEBUG 字段，值为 false
            buildConfigField(type = "Boolean", name = "DEBUG", value = "false")
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
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("me.onebone:toolbar-compose:2.3.5")
    implementation("com.google.dagger:hilt-android:2.50")
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.compose.ui.geometry)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.ui.unit)
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