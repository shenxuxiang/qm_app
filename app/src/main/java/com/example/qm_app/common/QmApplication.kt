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
                if (!::_commonViewModel.isInitialized) {
                    _commonViewModel = viewModel
                } else {
                    throw RuntimeException("[QmApplication CommonViewModel] has been initialized and cannot be duplicated")
                }
            }

        private lateinit var _navController: NavController

        /**
         * 全局 NavController，在 MainActivity 中进行赋值，只允许被赋值一次
         * */
        var navController: NavController
            get() = _navController
            set(controller) {
                if (!::_navController.isInitialized) {
                    _navController = controller
                } else {
                    throw RuntimeException("[QmApplication NavController] has been initialized and cannot be duplicated")
                }
            }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}