package com.example.qm_app.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.qm_app.CommonViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QmApplication() : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var _context: Context

        var context: Context
            get() = _context
            set(ctx) {
                if (!::_context.isInitialized) {
                    _context = ctx
                } else {
                    throw RuntimeException("[QmApplication Context] has been initialized and cannot be duplicated")
                }
            }

        private lateinit var _commonViewModel: CommonViewModel
        var commonViewModel: CommonViewModel
            get() = _commonViewModel
            set(viewModel) {
                if (!::_commonViewModel.isInitialized) {
                    _commonViewModel = viewModel
                } else {
                    throw RuntimeException("[QmApplication CommonViewModel] has been initialized and cannot be duplicated")
                }
            }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}