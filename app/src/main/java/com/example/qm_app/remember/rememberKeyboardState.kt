package com.example.qm_app.remember

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalView

enum class KeyboardState { Opened, Closed }

class DefaultKeyboardState(initialValue: KeyboardState = KeyboardState.Closed) {
    private val _state = mutableStateOf(initialValue)
    val state get() = _state.value

    fun update(state: KeyboardState) {
        _state.value = state
    }

    companion object {
        val Saver: Saver<DefaultKeyboardState, KeyboardState> =
            Saver(
                save = {
                    it.state
                },
                restore = {
                    DefaultKeyboardState(it)
                }
            )
    }
}

@Composable
fun rememberKeyboardState(): KeyboardState {
    val view = LocalView.current
    val keyboardState = rememberSaveable(saver = DefaultKeyboardState.Saver) {
        DefaultKeyboardState()
    }

    DisposableEffect(Unit) {
        val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            // 创建一个空的 Rect 对象
            val rect = Rect()
            // 它将当前窗口可见区域的坐标填入 rect 对象
            // 这个【可见区域】不包括被系统 UI（如状态栏、导航栏、软键盘）遮挡的部分
            view.getWindowVisibleDisplayFrame(rect)
            // 获取根视图的完整高度（整个屏幕高度，含状态栏、导航栏部分）
            val screenHeight = view.rootView.height
            val keyboardHeight = screenHeight - rect.bottom
            // 如果键盘高度超过屏幕高度的 25%，则认为键盘显示
            keyboardState.update(if (keyboardHeight > screenHeight * 0.25) KeyboardState.Opened else KeyboardState.Closed)
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    return keyboardState.state
}