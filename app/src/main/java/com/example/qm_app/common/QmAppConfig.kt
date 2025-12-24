package com.example.qm_app.common

import com.example.qm_app.BuildConfig

object QmAppConfig {
    val BASE_URL: String = BuildConfig.BASE_URL

    val isDevelopment = BuildConfig.BUILD_TYPE == "debug"

    val isProduction = BuildConfig.BUILD_TYPE == "release"
}