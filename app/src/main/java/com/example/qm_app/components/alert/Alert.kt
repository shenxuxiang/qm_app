package com.example.qm_app.components.alert

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


sealed class AlertContent {
    data class TextContent(val message: String) : AlertContent()
    class ComposableContent(val content: @Composable () -> Unit) : AlertContent()
}

class AlertUiEvent private constructor(
    val content: AlertContent,
    val showCancel: Boolean = true,
    val showConfirm: Boolean = true,
    val cancelText: String = "取消",
    val confirmText: String = "确定",
    val onCancel: (() -> Unit)? = null,
    val onConfirm: (() -> Unit)? = null,
    val footer: (@Composable () -> Unit)? = null,
) {
    constructor(
        text: String,
        showCancel: Boolean = true,
        showConfirm: Boolean = true,
        cancelText: String = "取消",
        confirmText: String = "确定",
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        footer: (@Composable () -> Unit)? = null,
    ) : this(
        content = AlertContent.TextContent(text),
        showCancel,
        showConfirm,
        cancelText,
        confirmText,
        onCancel,
        onConfirm,
        footer,
    )

    constructor(
        content: @Composable () -> Unit,
        showCancel: Boolean = true,
        showConfirm: Boolean = true,
        cancelText: String = "取消",
        confirmText: String = "确定",
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        footer: (@Composable () -> Unit)? = null,
    ) : this(
        content = AlertContent.ComposableContent(content),
        showCancel,
        showConfirm,
        cancelText,
        confirmText,
        onCancel,
        onConfirm,
        footer,
    )
}

object Alert {
    private val _coroutineScope = CoroutineScope(context = Dispatchers.Default)

    object EventBus {
        private val _event = MutableSharedFlow<AlertUiEvent>()
        val event = _event.asSharedFlow()

        suspend fun emit(event: AlertUiEvent) {
            _event.emit(event)
        }

        fun postEmit(event: AlertUiEvent) {
            _coroutineScope.launch {
                _event.emit(event)
            }
        }
    }

    fun confirm(
        content: @Composable () -> Unit,
        showCancel: Boolean = true,
        showConfirm: Boolean = true,
        cancelText: String = "取消",
        confirmText: String = "确定",
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        footer: (@Composable () -> Unit)? = null,
    ) {
        EventBus.postEmit(
            event = AlertUiEvent(
                content,
                showCancel,
                showConfirm,
                cancelText,
                confirmText,
                onCancel,
                onConfirm,
                footer,
            )
        )
    }

    fun confirm(
        text: String,
        showCancel: Boolean = true,
        showConfirm: Boolean = true,
        cancelText: String = "取消",
        confirmText: String = "确定",
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        footer: (@Composable () -> Unit)? = null,
    ) {
        EventBus.postEmit(
            event = AlertUiEvent(
                text,
                showCancel,
                showConfirm,
                cancelText,
                confirmText,
                onCancel,
                onConfirm,
                footer,
            )
        )
    }
}