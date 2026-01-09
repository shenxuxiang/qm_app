package com.example.qm_app.components.loading

import android.view.View
import androidx.compose.runtime.mutableStateOf
import com.example.qm_app.common.Overlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object Loading {
    private val _coroutineScope = CoroutineScope(context = Dispatchers.Main + SupervisorJob())

    sealed class UiEvent {
        data class ShowLoading(val message: String, val view: View? = null) : UiEvent()
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


    init {
        val visible = mutableStateOf(false)
        _coroutineScope.launch {
            EventBus.event.collect { event ->
                when (event) {
                    is UiEvent.HideLoading -> {
                        visible.value = false
                    }

                    is UiEvent.ShowLoading -> {
                        visible.value = true
                        lateinit var dispose: () -> Unit
                        dispose = Overlay.create(event.view) {
                            LoadingWidget(
                                visible = visible,
                                message = event.message,
                                onClose = { dispose() },
                            )
                        }
                    }
                }
            }
        }
    }

    suspend fun show(message: String = "数据加载中···", view: View? = null) {
        EventBus.emit(UiEvent.ShowLoading(message, view))
    }

    suspend fun hide() {
        EventBus.emit(UiEvent.HideLoading)
    }

    fun postShow(message: String = "数据加载中···", view: View? = null) {
        EventBus.postEmit(UiEvent.ShowLoading(message, view))
    }

    fun postHide() {
        EventBus.postEmit(UiEvent.HideLoading)
    }
}