package com.example.qm_app.components

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.Overlay
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white
import kotlinx.coroutines.launch

sealed class AlertContent {
    data class TextContent(val message: String) : AlertContent()
    class ComposableContent(val content: @Composable (() -> Unit) -> Unit) : AlertContent()
}

class AlertUiEvent private constructor(
    val content: AlertContent,
    val showCancel: Boolean,
    val showConfirm: Boolean,
    val cancelText: String,
    val confirmText: String,
    val onCancel: (() -> Unit)?,
    val onConfirm: (() -> Unit)?,
    val footer: @Composable ((() -> Unit) -> Unit)?,
    val alignment: Alignment,
    val contentPadding: PaddingValues,
    val corner: Shape,
) {
    constructor(
        text: String,
        showCancel: Boolean = true,
        showConfirm: Boolean = true,
        cancelText: String = "取消",
        confirmText: String = "确定",
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        footer: (@Composable (() -> Unit) -> Unit)? = null,
        alignment: Alignment,
        contentPadding: PaddingValues,
        corner: Shape,
    ) : this(
        content = AlertContent.TextContent(text),
        showCancel,
        showConfirm,
        cancelText,
        confirmText,
        onCancel,
        onConfirm,
        footer,
        alignment,
        contentPadding,
        corner,
    )

    constructor(
        content: @Composable ((() -> Unit) -> Unit),
        showCancel: Boolean = true,
        showConfirm: Boolean = true,
        cancelText: String = "取消",
        confirmText: String = "确定",
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        footer: @Composable ((() -> Unit) -> Unit)? = null,
        alignment: Alignment,
        contentPadding: PaddingValues,
        corner: Shape,
    ) : this(
        content = AlertContent.ComposableContent(content),
        showCancel,
        showConfirm,
        cancelText,
        confirmText,
        onCancel,
        onConfirm,
        footer,
        alignment,
        contentPadding,
        corner,
    )

    @Composable
    fun create(dispose: () -> Unit) {
        val initialScale = 0.2f
        val animationMillis = 200
        val initialOffset = with(LocalDensity.current) { (-150).dp.toPx() }

        val showAlert = remember { mutableStateOf(false) }
        val hasConfirm = remember { mutableStateOf(false) }
        val alpha by animateFloatAsState(
            targetValue = if (showAlert.value) 1f else 0f,
            animationSpec = tween(durationMillis = animationMillis),
            finishedListener = { alpha ->
                if (alpha == 0f) dispose()
                // 是否已经确认
                if (hasConfirm.value) onConfirm?.invoke() else onCancel?.invoke()
            }
        )
        val scaleAnimate = remember { Animatable(initialScale) }
        val offsetAnimate = remember { Animatable(initialOffset) }

        LaunchedEffect(Unit) {
            showAlert.value = true
            launch {
                scaleAnimate.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = animationMillis)
                )
            }
            launch {
                offsetAnimate.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = animationMillis)
                )
            }
        }

        // 系统返回键拦截
        val onBackPressedDispatcher =
            LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        DisposableEffect(Unit) {
            var onBackPressedCallback: OnBackPressedCallback? = null
            onBackPressedDispatcher?.let {
                onBackPressedCallback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        showAlert.value = false
                    }
                }
                onBackPressedDispatcher.addCallback(onBackPressedCallback)
            }

            onDispose {
                onBackPressedCallback?.remove()
            }
        }

        Box(
            contentAlignment = alignment,
            modifier = Modifier
                .alpha(alpha)
                .fillMaxSize()
                .background(color = black.copy(alpha = 0.4f)),
        ) {
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth()
                    .graphicsLayer(
                        translationY = offsetAnimate.value,
                        scaleX = scaleAnimate.value,
                        scaleY = scaleAnimate.value,
                        alpha = alpha
                    )
                    .clip(corner)
                    .background(color = white)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Body 部分
                    if (content is AlertContent.TextContent) {
                        Text(
                            text = content.message,
                            color = black4,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier
                                .padding(vertical = 28.dp, horizontal = 31.dp)
                                .fillMaxWidth(),
                        )
                    } else {
                        (content as AlertContent.ComposableContent).content {
                            showAlert.value = false
                        }
                    }
                    // Footer 部分
                    if (footer == null) {
                        Row(
                            modifier = Modifier.padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            if (showCancel)
                                ButtonWidget(
                                    text = cancelText,
                                    type = ButtonWidgetType.Default,
                                    onTap = { showAlert.value = false },
                                    modifier = Modifier
                                        .padding(horizontal = 9.dp)
                                        .size(width = 120.dp, height = 36.dp),
                                )

                            if (showConfirm)
                                ButtonWidget(
                                    text = confirmText,
                                    type = ButtonWidgetType.Primary,
                                    modifier = Modifier
                                        .padding(horizontal = 9.dp)
                                        .size(120.dp, 36.dp),
                                    onTap = {
                                        hasConfirm.value = true
                                        showAlert.value = false
                                    },
                                )
                        }
                    } else {
                        footer { showAlert.value = false }
                    }
                }
            }
        }
    }
}

object Alert {
    fun confirm(
        view: View,
        content: @Composable (() -> Unit) -> Unit,
        showCancel: Boolean = true,
        showConfirm: Boolean = true,
        cancelText: String = "取消",
        confirmText: String = "确定",
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        footer: (@Composable (() -> Unit) -> Unit)? = null,
        alignment: Alignment = BiasAlignment(0f, -0.2f),
        contentPadding: PaddingValues = PaddingValues(horizontal = 33.dp),
        corner: Shape = corner10,
    ) {
        lateinit var dispose: () -> Unit
        dispose = Overlay.create(view) {
            AlertUiEvent(
                content,
                showCancel,
                showConfirm,
                cancelText,
                confirmText,
                onCancel,
                onConfirm,
                footer,
                alignment,
                contentPadding,
                corner,
            ).create(dispose = { dispose() })
        }
    }

    fun confirm(
        view: View,
        text: String,
        showCancel: Boolean = true,
        showConfirm: Boolean = true,
        cancelText: String = "取消",
        confirmText: String = "确定",
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        footer: (@Composable (() -> Unit) -> Unit)? = null,
        alignment: Alignment = BiasAlignment(0f, -0.2f),
        contentPadding: PaddingValues = PaddingValues(horizontal = 33.dp),
        corner: Shape = corner10,
    ) {
        lateinit var dispose: () -> Unit
        dispose = Overlay.create(view) {
            AlertUiEvent(
                text,
                showCancel,
                showConfirm,
                cancelText,
                confirmText,
                onCancel,
                onConfirm,
                footer,
                alignment,
                contentPadding,
                corner,
            ).create(dispose = { dispose() })
        }
    }
}