package com.example.qm_app.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpClient {
    fun create(timeout: Long, vararg interceptors: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().run {
            connectTimeout(timeout, unit = TimeUnit.SECONDS)
            for (interceptor in interceptors) addInterceptor(interceptor)
            build()
        }
    }
}