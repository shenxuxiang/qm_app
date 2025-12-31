package com.example.qm_app.components.loading

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object Loading {
    private val _coroutineScope = CoroutineScope(Dispatchers.Default)

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
            _coroutineScope.launch {
                _event.emit(event)
            }
        }
    }

    suspend fun show(message: String = "数据加载中···") {
        EventBus.emit(UiEvent.ShowLoading(message))
    }

    suspend fun hide() {
        EventBus.emit(UiEvent.HideLoading)
    }

    fun postShow(message: String = "数据加载中···") {
        EventBus.postEmit(UiEvent.ShowLoading(message))
    }

    fun postHide() {
        EventBus.postEmit(UiEvent.HideLoading)
    }
}