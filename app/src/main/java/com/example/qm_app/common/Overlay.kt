package com.example.qm_app.common

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.qm_app.ui.theme.QmTheme

object Overlay {
    fun create(
        view: View? = null,
        content: @Composable () -> Unit,
    ): () -> Unit {
        val anchorView = view ?: QmApplication.activity.window.decorView
        // 创建一个帧布局
        val frameLayout = FrameLayout(anchorView.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            setBackgroundColor(Color.Transparent.toArgb())
        }

        // 创建一个 ComposeView 容器
        val composeView = ComposeView(anchorView.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            }

            setViewTreeLifecycleOwner(anchorView.findViewTreeLifecycleOwner())
            setViewTreeViewModelStoreOwner(anchorView.findViewTreeViewModelStoreOwner())
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            setViewTreeSavedStateRegistryOwner(anchorView.findViewTreeSavedStateRegistryOwner())
        }

        // 移除
        val dispose = {
            frameLayout.removeView(composeView)
            (anchorView.rootView as ViewGroup).removeView(frameLayout)
            // 释放掉 ComposeView 所占用的资源
            composeView.disposeComposition()
        }

        composeView.setContent {
            QmTheme {
                content()
            }
        }

        frameLayout.addView(composeView)
        (anchorView.rootView as ViewGroup).addView(frameLayout)

        return dispose
    }
}