package com.example.qm_app.components.toast

import android.view.View
import com.example.qm_app.common.Overlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object Toast {
    // 协程
    private val _coroutineScope = CoroutineScope(context = Dispatchers.Main + SupervisorJob())

    // Toast 类型
    enum class ToastType { Default, Warning, Success }

    // Toast 事件对象，信息来源
    class UiEvent(
        val message: String,
        val duration: Int,
        val type: ToastType,
        val view: View? = null,
    )

    // 事件总线
    object EventBus {
        private val _event = MutableSharedFlow<UiEvent>(
            replay = 1,
            extraBufferCapacity = 9, // 最多 9 个
            onBufferOverflow = BufferOverflow.SUSPEND
        )
        val event = _event.asSharedFlow()

        suspend fun emit(event: UiEvent) {
            _event.emit(event)
        }

        fun postEmit(event: UiEvent) {
            _coroutineScope.launch {
                _event.emit(event)
            }
        }
    }

    init {
        _coroutineScope.launch {
            EventBus.event.collect { event ->
                lateinit var dispose: () -> Unit
                dispose = Overlay.create(event.view) {
                    ToastWidget(
                        toastType = event.type,
                        message = event.message,
                        onClose = { dispose() },
                        duration = event.duration,
                    )
                }
                delay(event.duration.toLong())
                delay(300)
            }
        }
    }

    suspend fun showToast(
        message: String,
        view: View? = null,
        duration: Int = 2000,
        type: ToastType = ToastType.Default,
    ) {
        EventBus.emit(event = UiEvent(message, duration, type, view))
    }

    suspend fun showSuccessToast(message: String, duration: Int = 2000, view: View? = null) {
        showToast(message, view, duration, type = ToastType.Success)
    }

    suspend fun showWarningToast(message: String, duration: Int = 2000, view: View? = null) {
        showToast(message, view, duration, type = ToastType.Warning)
    }

    fun postShowToast(
        message: String,
        duration: Int = 2000,
        type: ToastType = ToastType.Default,
        view: View? = null,
    ) {
        EventBus.postEmit(event = UiEvent(message, duration, type, view))
    }

    fun postShowSuccessToast(message: String, duration: Int = 2000, view: View? = null) {
        postShowToast(message, duration, type = ToastType.Success, view)
    }

    fun postShowWarningToast(message: String, duration: Int = 2000) {
        postShowToast(message, duration, type = ToastType.Warning)
    }
}