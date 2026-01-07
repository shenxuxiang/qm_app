package com.example.qm_app.components.loading

import androidx.compose.runtime.mutableStateOf
import com.example.qm_app.components.InsertAndroidViewManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object Loading {
    private val _coroutineScope = CoroutineScope(context = Dispatchers.Main + SupervisorJob())

    sealed class UiEvent {
        data class ShowLoading(val message: String) : UiEvent()
        object HideLoading : UiEvent()
    }

    object EventBus {
        private val _event = MutableSharedFlow<UiEvent>(
            replay = 1,
            extraBufferCapacity = 9,
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
        val visible = mutableStateOf(false)
        _coroutineScope.launch {
            EventBus.event.collect { event ->
                _channel.send(element = event)
            }
        }

        _coroutineScope.launch {
            for (item in _channel) {
                when (item) {
                    is UiEvent.HideLoading -> {
                        visible.value = false
                    }

                    is UiEvent.ShowLoading -> {
                        visible.value = true
                        lateinit var removeChild: () -> Unit
                        removeChild = InsertAndroidViewManager.appendChild {
                            LoadingWidget(
                                visible = visible,
                                message = item.message,
                                onClose = { removeChild() },
                            )
                        }
                    }
                }
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