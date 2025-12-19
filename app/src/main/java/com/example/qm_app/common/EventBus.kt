package com.example.qm_app.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    suspend fun emitEvent(event: UiEvent) { // 使用 emit 是挂起函数
        _event.emit(value = event)
    }

    fun postEvent(event: UiEvent) { // 或用 tryEmit 在非挂起上下文中尝试发射
        _event.tryEmit(value = event)
    }
}