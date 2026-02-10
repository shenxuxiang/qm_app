package com.example.qm_app.router

import android.graphics.Bitmap
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.savedstate.savedState
import com.example.qm_app.common.ScreenShotUtils
import com.google.gson.Gson

object Router {
    data class BackResult<T>(val data: T, val source: String)

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
    fun navigate(route: String, parameters: Map<String, String?>? = null) {
        _previousScreenShotBitmap = ScreenShotUtils.captureFullScreen()

        var path = route
        if (parameters != null) {
            path = replace(path, parameters)
        }

        _navController.navigate(path) {
            launchSingleTop = true
            savedState()
        }
    }

    fun navigate(
        route: String,
        parameters: Map<String, String?>? = null,
        builder: NavOptionsBuilder.() -> Unit,
    ) {
        _previousScreenShotBitmap = ScreenShotUtils.captureFullScreen()

        var path = route
        if (parameters != null) {
            path = replace(path, parameters)
        }

        _navController.navigate(path, builder)
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

    fun <T> popBackStack(result: BackResult<T>): Boolean {
        val gson = Gson()
        val json = gson.toJson(result)
        _navController.previousBackStackEntry?.savedStateHandle?.set(
            "arguments",
            json,
        )

        return _navController.popBackStack()
    }

    /**
     * 返回到指定页面
     * */
    fun popBackStack(route: String, inclusive: Boolean, saveState: Boolean): Boolean {
        return _navController.popBackStack(route, inclusive, saveState)
    }

    /**
     * 路由中的占位符替换
     * */
    private fun replace(pattern: String, replacement: Map<String, String?>): String {
        var result = pattern
        for ((k, v) in replacement) {
            val reg = """\{${k}\}""".toRegex()
            result = result.replace(reg, v ?: "")
        }

        // pattern 模板中的占位符必须和 replacement 中的 Pair 一一对应，否则抛出异常。
        val reg = Regex("""\{(?<key>[a-zA-Z]+)\}""")
        val matched = reg.find(result)
        if (matched is MatchResult) {
            val key = matched.groups["key"]!!.value
            throw Error("The Route Pattern is '$pattern', But You Missed Passing The Parameter $key")
        }

        return result
    }
}