import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")  // 添加这行
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

fun buildVersionCode(): Int? {
    // 这是启动参数中的一个属性，它返回一个 List<String>，包含了在命令行中指定的任务名称。
    val taskNames = gradle.startParameter.taskNames
    // taskName 的格式为：assemble + flavorName + buildType
    // 它的值一般为：assembleProdRelease、assembleDevRelease 等。
    var taskName = taskNames.find { task -> task.startsWith("assemble") } ?: ""
    val regex = """^(?<flavorName>[A-Z][a-z]+)(?<buildType>[A-Z][a-z]+)$""".toRegex()

    taskName = taskName.replace("^assemble".toRegex(), "")

    return regex.matchEntire(taskName)?.let { matched ->
        val flavorName = matched.groups["flavorName"]!!.value.lowercase()
        val buildType = matched.groups["buildType"]!!.value.lowercase()
        // 如果是开发者模式，则不需要执行以下步骤。
        if (buildType == "debug") return null

        // 注意：projectDir 对应项目的 `app/` 路径
        val propertiesFileName =
            "${project.projectDir.path}/properties/${flavorName}_${buildType}.properties"
        val propertiesFile = File(propertiesFileName)
        val props = Properties()

        if (propertiesFile.exists()) {
            props.load(FileInputStream(propertiesFile))
        } else {
            props["versionCode"] = "0"
            props.store(FileOutputStream(propertiesFile), null)
        }

        // 每次构建时，都在原来的基础之上加 1
        val nextVersionCode = props["versionCode"].toString().toInt() + 1
        props["versionCode"] = nextVersionCode.toString()
        props.store(FileOutputStream(propertiesFile), null)

        return nextVersionCode
    } ?: run {
        return@run null
    }
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["appName"] = "阡陌农服"

        // 获取构建版本号
        val code = buildVersionCode()
        if (code == null) {
            versionCode = 1
            versionName = "1.0"
        } else {
            versionCode = code
            versionName = "1.0.${code}"
        }
    }

    flavorDimensions.add("environment")
    // 定义环境维度下的风味
    productFlavors {
        create("dev") {
            // 属于环境维度
            dimension = "environment"
            // 包名后缀
            // applicationIdSuffix = ".dev"
            // 版本名后缀
            // versionNameSuffix = "-alpha"

            buildConfigField(type = "String", name = "ENVIRONMENT", value = "\"development\"")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = """"http://60.169.69.3:30062""""
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
                value = """"http://60.169.69.3:30066""""
            )
            resValue(type = "string", name = "app_name", value = "阡陌农服")
        }
    }

    // 配置签名文件
    signingConfigs {
        getByName("debug") {
            storeFile = file("./debug-keystore.jks")
            keyAlias = "debug-keystore"
            storePassword = "sqal@145680"
            keyPassword = "sqal@145680"
        }
        create("release") {
            storeFile = file("./release-keystore.jks")
            keyAlias = "release-keystore"
            storePassword = "sqal@145680"
            keyPassword = "sqal@145680"
        }
    }

    buildTypes {
        debug {
            // 由于我们修改了 debug 签名，所以这里需要显示指定 debug 构建类型的签名。
            // 否则，系统将继续使用默认的 debug 配置签名: 使用 ~/.android/debug.keystore
            signingConfig = signingConfigs.getByName("debug")
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
        viewBinding = true
    }

    // 分包
    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = false
        }
    }

    // 遍历所有的变体，applicationVariants 是一个表示应用变体的集合（例如，debug、release等）。
    // 注意，applicationVariants 表示当前配置中所有的变体，并不是当前构建的变体。
    applicationVariants.all {
        // 获取变体的构建类型、产品风味、版本名称
        val buildType = this.buildType.name
        val flavorName = this.flavorName
        val versionName = this.versionName
        // 每个变体可能有多个输出（例如，多个 APK、AAB 等）。
        outputs.all {
            // 类型检查，确保处理的是 APK 输出。
            // 注意：BaseVariantOutputImpl 类型表示 APK 输出，这个类是一个内部 API。
            if (this is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                // 查找过滤器类型为 ABI 的过滤器
                // filter?.identifier 就是我们想要的 apk 的格式：比如 arm64-v8a
                val abiType = this.filters.find { it.filterType == "ABI" }?.identifier ?: ""
                this.outputFileName =
                    "app-${flavorName}-${buildType}-${abiType}-v${versionName}.apk"
            }
        }
    }
}

dependencies {
    implementation("androidx.camera:camera-extensions:1.5.2")
    implementation("androidx.camera:camera-effects:1.5.2")
    implementation("androidx.camera:camera-video:1.5.2")
    implementation("androidx.camera:camera-camera2:1.5.2")
    implementation("androidx.camera:camera-view:1.5.2")
    implementation("androidx.camera:camera-core:1.5.2")
    implementation("androidx.camera:camera-lifecycle:1.5.2")
    implementation("androidx.camera:camera-compose:1.5.2")
    implementation("com.github.promeg:tinypinyin:2.0.3")
    implementation("com.holix.android:bottomsheetdialog-compose:1.5.0")
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
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.play.services.auth.api.phone)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.camera.core)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3)
    implementation(files("src\\main\\jniLibs\\AMap3DMap_10.1.600_AMapSearch_9.7.4_AMapLocation_6.5.1_20251020.jar"))
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