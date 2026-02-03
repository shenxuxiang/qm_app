package com.example.qm_app.common

import com.example.qm_app.BuildConfig

object QmAppConfig {
    const val baseURL: String = BuildConfig.BASE_URL

    const val isDev = BuildConfig.BUILD_TYPE == "debug"

    const val isProd = BuildConfig.BUILD_TYPE == "release"
}