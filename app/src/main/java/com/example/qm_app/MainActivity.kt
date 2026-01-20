package com.example.qm_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.qm_app.common.QmApplication
import com.example.qm_app.common.ScreenShotUtils
import com.example.qm_app.router.Route
import com.example.qm_app.router.RouterHost
import com.example.qm_app.ui.theme.QmTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ScreenShotUtils.init(this)
        QmApplication.activity = this
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            QmTheme {
                // 获取 SystemUiController 实例，它是管理状态栏和导航栏外观的核心工具。
                val systemUiController = rememberSystemUiController()
                // darkIcons: true-深色图标(浅色背景), false-浅色图标(深色背景)
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = true
                )
                systemUiController.setNavigationBarColor(
                    color = Color.Black,
                    darkIcons = false
                )

                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    RouterHost(startDestination = Route.MainScreen.route)
                }
            }
        }
    }
}