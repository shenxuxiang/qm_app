package com.example.qm_app.common

import com.example.qm_app.BuildConfig

object QmAppConfig {
    val baseURL: String = BuildConfig.BASE_URL

    val isDev = BuildConfig.BUILD_TYPE == "debug"

    val isProd = BuildConfig.BUILD_TYPE == "release"
}