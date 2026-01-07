package com.example.qm_app.router

import android.graphics.Bitmap
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.savedstate.savedState
import com.example.qm_app.common.ScreenShotUtils

object Router {
    private lateinit var _navController: NavHostController

    /**
     * 路由控制器
     * */
    val controller: NavHostController get() = _navController

    /**
     * 屏幕截图
     * */
    private var _previousScreenShotBitmap: Bitmap? = null

    val previousScreenShotBitmap: Bitmap? get() = _previousScreenShotBitmap

    fun init(controller: NavHostController) {
        _navController = controller
    }

    /**
     * 导航到目标页面
     * 设置启动模式为 singleTop，避免用户多次点击跳转按钮，导致同一个页面在返回栈中多次加载
     * */
    fun navigate(route: String) {
        _previousScreenShotBitmap = ScreenShotUtils.captureFullScreen()
        _navController.navigate(route) {
            launchSingleTop = true
            savedState()
        }
    }

    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit) {
        _previousScreenShotBitmap = ScreenShotUtils.captureFullScreen()
        _navController.navigate(route, builder)
    }

    /**
     * 返回上一页
     * */
    fun navigateUp(): Boolean {
        return _navController.navigateUp()
    }

    /**
     * 返回上一页
     * */
    fun popBackStack(): Boolean {
        return _navController.popBackStack()
    }

    /**
     * 返回到指定页面
     * */
    fun popBackStack(route: String, inclusive: Boolean, saveState: Boolean): Boolean {
        return _navController.popBackStack(route, inclusive, saveState)
    }
}