package com.example.qm_app.components

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.qm_app.router.Router
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


object SwipeBackEventBus {
    class BackEvent()

    private val _event = MutableSharedFlow<BackEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND,
    )
    val event = _event.asSharedFlow()

    suspend fun emit() {
        _event.emit(BackEvent())
    }
}

@Composable
fun SwipeBack(content: @Composable () -> Unit) {
    var isStart = remember { false }
    val density = LocalDensity.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    val coroutineScope = rememberCoroutineScope()
    val translationX = remember { Animatable(0f) }
    val edge = remember { with(density) { 50.dp.toPx() } }
    val threshold = remember { with(density) { 100.dp.toPx() } }
    val canBack = remember { Router.controller.previousBackStackEntry != null }
    val screenShot = rememberSaveable { Router.previousScreenShotBitmap as Bitmap } as Bitmap?

    fun onDragEnd() {
        if (isStart && canBack) {
            isStart = false
            if (translationX.value > threshold) {
                coroutineScope.launch {
                    SwipeBackEventBus.emit()
                    translationX.animateTo(
                        targetValue = with(density) { screenWidthDp.toPx() },
                        animationSpec = tween(200)
                    )
                    Router.popBackStack()
                    screenShot?.recycle()
                }
            } else {
                coroutineScope.launch {
                    translationX.animateTo(targetValue = 0f)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (canBack)
            AsyncImage(
                model = screenShot,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(translationX = translationX.value)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset -> isStart = offset.x <= edge },
                        onDragEnd = {
                            if (isStart && canBack) onDragEnd()
                        },
                        onDragCancel = {
                            if (isStart && canBack) onDragEnd()
                        },
                        onHorizontalDrag = { _, offset ->
                            if (isStart && canBack) {
                                val dist = translationX.value + offset
                                coroutineScope.launch {
                                    translationX.snapTo(targetValue = dist)
                                }
                            }
                        }
                    )
                }
        ) { content() }
    }
}