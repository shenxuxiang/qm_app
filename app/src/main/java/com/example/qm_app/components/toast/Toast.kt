package com.example.qm_app.components.toast

import com.example.qm_app.components.InsertAndroidViewManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object Toast {
    private val _coroutineScope = CoroutineScope(context = Dispatchers.Main + SupervisorJob())

    enum class ToastType { Default, Warning, Success }
    class UiEvent(val message: String, val duration: Int, val type: ToastType)
    object EventBus {
        private val _event = MutableSharedFlow<UiEvent>(
            replay = 1,
            extraBufferCapacity = 99,
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

    private val _channel = Channel<UiEvent>(Channel.UNLIMITED)

    init {
        _coroutineScope.launch {
            EventBus.event.collect { event ->
                _channel.send(element = event)
            }
        }

        _coroutineScope.launch {
            for (item in _channel) {
                lateinit var removeChild: () -> Unit
                removeChild = InsertAndroidViewManager.appendChild {
                    ToastWidget(
                        message = item.message,
                        duration = item.duration,
                        onClose = { removeChild() },
                        toastType = item.type,
                    )
                }
                delay(item.duration.toLong())
                delay(300)
            }
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

    fun postShowToast(message: String, duration: Int = 2000, type: ToastType = ToastType.Default) {
        EventBus.postEmit(event = UiEvent(message, duration, type))
    }

    fun postShowSuccessToast(message: String, duration: Int = 2000) {
        postShowToast(message, duration, type = ToastType.Success)
    }

    fun postShowWarningToast(message: String, duration: Int = 2000) {
        postShowToast(message, duration, type = ToastType.Warning)
    }
}