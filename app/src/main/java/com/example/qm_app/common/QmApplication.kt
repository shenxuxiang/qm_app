package com.example.qm_app.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.navigation.NavController
import com.example.qm_app.CommonViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QmApplication() : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        private lateinit var _commonViewModel: CommonViewModel

        /**
         * 全局 ViewModel，在 MainActivity 中进行赋值，只允许被赋值一次
         * */
        var commonViewModel: CommonViewModel
            get() = _commonViewModel
            set(viewModel) {
                _commonViewModel = viewModel
            }

        private lateinit var _navController: NavController
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}