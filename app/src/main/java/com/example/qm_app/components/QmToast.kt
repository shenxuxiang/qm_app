package com.example.qm_app.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.EventBus
import com.example.qm_app.common.QmIcons
import com.example.qm_app.common.ToastType
import com.example.qm_app.common.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ToastTask(val message: String, val duration: Int, val type: ToastType)

@Composable
fun QmToast(durationMillis: Int = 300) {
    val initialScale = 0.6f
    val initialOffset = (-50).dp
    val maxWidth = (LocalConfiguration.current.screenWidthDp * 0.7).dp

    val message = remember { mutableStateOf("") }
    val isShow = remember { mutableStateOf(false) }
    val messageType = remember { mutableStateOf(ToastType.Default) }

    val alpha = animateFloatAsState(targetValue = if (isShow.value) 1f else 0f)
    val scaleAnimate = remember { Animatable(initialScale) }
    val offsetAnimate = remember { Animatable(initialOffset, Dp.VectorConverter) }
    val messageQueue = remember { Channel<ToastTask>(Channel.UNLIMITED) }

    LaunchedEffect(Unit) {
        EventBus.event.collect { event ->
            if (event is UiEvent.ShowToast) {
                messageQueue.trySend(
                    element = ToastTask(
                        type = event.type,
                        message = event.message,
                        duration = event.duration,
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        for (item in messageQueue) {
            isShow.value = true
            message.value = item.message
            messageType.value = item.type
            launch {
                scaleAnimate.animateTo(1f, animationSpec = tween(durationMillis))
            }
            launch {
                offsetAnimate.animateTo(0.dp, animationSpec = tween(durationMillis))
            }
            delay(item.duration.toLong())
            isShow.value = false
            delay(300)
        }
    }

    LaunchedEffect(alpha.value) {
        if (alpha.value == 0f) {
            scaleAnimate.snapTo(initialScale)
            offsetAnimate.snapTo(initialOffset)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = BiasAlignment(0f, -0.5f),
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .graphicsLayer(
                    translationX = 0f,
                    translationY = with(LocalDensity.current) { offsetAnimate.value.toPx() },
                    alpha = alpha.value,
                    scaleX = scaleAnimate.value,
                    scaleY = scaleAnimate.value,
                )
                .background(
                    color = Color(0xFF000000).copy(alpha = 0.7f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (messageType.value) {
                    ToastType.Success -> {
                        QmIcon(
                            icon = QmIcons.Stroke.Success,
                            size = 22.dp,
                            tint = Color(0xFF3AC786),
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }

                    ToastType.Warning -> {
                        QmIcon(
                            icon = QmIcons.Stroke.Warn,
                            size = 22.dp,
                            tint = Color(0xFFFF4D4F),
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }

                    else -> {}
                }
                Text(
                    text = message.value,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    maxLines = if (messageType.value == ToastType.Default) 2 else 1,
                    modifier = Modifier.widthIn(120.dp, maxWidth),
                )
            }
        }
    }
}

