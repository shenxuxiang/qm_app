package com.example.qm_app.common

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas

object ScreenShotUtils {
    private lateinit var _activity: Activity

    /**
     * 初始化
     * */
    fun init(activity: Activity) {
        _activity = activity
    }

    /**
     * 获取整个屏幕截图
     * */
    fun captureFullScreen(): Bitmap {
        if (!::_activity.isInitialized) throw RuntimeException("ScreenShotUtils Has Not Been Initialized Yet")

        // 获取根视图（屏幕的根布局）
        val view = _activity.window.decorView.rootView
        // 创建一个空白的位图
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )

        // 将创建的画布绑定到位图
        val canvas = Canvas(bitmap)
        // 开始绘制
        view.draw(canvas)
        return bitmap
    }
}