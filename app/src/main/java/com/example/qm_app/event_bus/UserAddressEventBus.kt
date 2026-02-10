package com.example.qm_app.event_bus

import com.example.qm_app.entity.UserAddressData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object UserAddressEventBus {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    sealed class UiEvent(val data: UserAddressData) {
        class Update(data: UserAddressData) : UiEvent(data)
    }

    private val _event = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val event = _event.asSharedFlow()

    suspend fun emit(event: UiEvent) {
        _event.emit(event)
    }

    fun postEmit(event: UiEvent) {
        coroutineScope.launch {
            _event.emit(event)
        }
    }
}