package com.example.qm_app.common

import android.util.Log
import com.example.qm_app.BuildConfig

object LogUntil {
    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private const val TAG = "QM_APP_LOG"

    // BuildConfig.DEBUG-true 开发环境、false-生产环境
    private var level = if (BuildConfig.DEBUG) VERBOSE else 0

    fun v(tag: String = TAG, msg: String) {
        if (level <= VERBOSE) {
            Log.v(tag, msg)
        }
    }


    fun d(tag: String = TAG, msg: String) {
        if (level <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String = TAG, msg: String) {
        if (level <= INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String = TAG, msg: String) {
        if (level <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String = TAG, msg: String) {
        if (level <= ERROR) {
            Log.e(tag, msg)
        }
    }
}