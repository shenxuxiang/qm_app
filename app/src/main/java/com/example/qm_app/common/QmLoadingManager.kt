package com.example.qm_app.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object QmLoadingManager {
    sealed class UiEvent {
        data class ShowLoading(val message: String) : UiEvent()
        object HideLoading : UiEvent()
    }

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

    suspend fun show(message: String) {
        EventBus.emit(UiEvent.ShowLoading(message))
    }

    suspend fun hide() {
        EventBus.emit(UiEvent.HideLoading)
    }
}