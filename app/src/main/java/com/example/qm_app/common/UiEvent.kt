package com.example.qm_app.common

enum class ToastType { Default, Warning, Success }
sealed class UiEvent {
    data class ShowToast(
        val message: String,
        val duration: Int = 3000,
        val type: ToastType = ToastType.Default,
    ) : UiEvent()
}