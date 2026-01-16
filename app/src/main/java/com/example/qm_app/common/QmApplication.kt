package com.example.qm_app.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import com.example.qm_app.pages.main.MainViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QmApplication() : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        private lateinit var _mainViewModel: MainViewModel

        /**
         * 全局 ViewModel，在 MainActivity 中进行赋值，只允许被赋值一次
         * */
        var mainViewModel: MainViewModel
            get() = _mainViewModel
            set(viewModel) {
                _mainViewModel = viewModel
            }


        private lateinit var _activity: Activity

        var activity: Activity
            get() = _activity
            set(activity) {
                _activity = activity
            }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}