package com.example.qm_app.components

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

object InsertAndroidViewManager {
    private lateinit var _frameLayout: FrameLayout
    private lateinit var _ctx: Context

    fun initialize(ctx: Context, frameLayout: FrameLayout) {
        _frameLayout = frameLayout
        _ctx = ctx
    }

    fun appendChild(content: @Composable () -> Unit): () -> Unit {
        val composeView = ComposeView(_ctx)
        composeView.setContent { content() }
        _frameLayout.addView(composeView)
        return fun() {
            _frameLayout.removeView(composeView)
        }
    }
}

@Composable
fun InsertAndroidView() {
    AndroidView(
        factory = { ctx ->
            val frameLayout = FrameLayout(ctx)
            InsertAndroidViewManager.initialize(ctx, frameLayout)
            frameLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            frameLayout.setBackgroundColor(Color.Transparent.toArgb())


            frameLayout
        }
    )

}