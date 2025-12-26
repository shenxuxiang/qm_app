package com.example.qm_app.common

import android.util.Log

object LogUntil {
    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private const val TAG = "QM_APP_LOG"

    // isDevelopment-true 开发环境、false-生产环境
    private var level = if (QmAppConfig.isDev) VERBOSE else 0

    fun v(msg: String, tag: String = TAG) {
        if (level <= VERBOSE) {
            Log.v(tag, msg)
        }
    }


    fun d(msg: String, tag: String = TAG) {
        if (level <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun d(exception: Exception, tag: String = TAG) {
        if (level <= DEBUG) {
            Log.d(tag, exception.message ?: "")
            for (item in exception.stackTrace) {
                Log.d(tag, item.toString())
            }
        }
    }

    fun i(msg: String, tag: String = TAG) {
        if (level <= INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(msg: String, tag: String = TAG) {
        if (level <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(msg: String, tag: String = TAG) {
        if (level <= ERROR) {
            Log.e(tag, msg)
        }
    }
}