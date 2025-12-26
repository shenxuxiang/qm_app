package com.example.qm_app.components.toast

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ToastManager {
    enum class ToastType { Default, Warning, Success }
    class UiEvent(val message: String, val duration: Int, val type: ToastType)
    object EventBus {
        private val _event = MutableSharedFlow<UiEvent>()
        val event = _event.asSharedFlow()

        suspend fun emit(event: UiEvent) {
            _event.emit(event)
        }

        fun postEmit(event: UiEvent) {
            _event.tryEmit(event)
        }
    }

    suspend fun showToast(
        message: String,
        duration: Int = 2000,
        type: ToastType = ToastType.Default,
    ) {
        EventBus.emit(event = UiEvent(message, duration, type))
    }

    suspend fun showSuccessToast(message: String, duration: Int = 2000) {
        showToast(message, duration, type = ToastType.Success)
    }

    suspend fun showWarningToast(message: String, duration: Int = 2000) {
        showToast(message, duration, type = ToastType.Warning)
    }

    fun showToastPost(message: String, duration: Int = 2000, type: ToastType = ToastType.Default) {
        EventBus.postEmit(event = UiEvent(message, duration, type))
    }

    fun showSuccessToastPost(message: String, duration: Int = 2000) {
        showToastPost(message, duration, type = ToastType.Success)
    }

    fun showWarningToastPost(message: String, duration: Int = 2000) {
        showToastPost(message, duration, type = ToastType.Warning)
    }
}