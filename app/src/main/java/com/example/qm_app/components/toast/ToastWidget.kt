package com.example.qm_app.components.toast

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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Stable
private data class ToastState(
    val message: String,
    val visible: Boolean,
    val type: Toast.ToastType,
)

@Composable
fun ToastWidget(
    animationDuration: Int = 300,
    alignment: Alignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.5f),
) {
    val initialScale = 0.6f
    val initialOffset = (-50).dp
    val maxWidth = (LocalConfiguration.current.screenWidthDp * 0.7).dp

    var toastState by remember {
        mutableStateOf(
            ToastState(
                message = "",
                visible = false,
                type = Toast.ToastType.Default,
            )
        )
    }

    val alpha = animateFloatAsState(targetValue = if (toastState.visible) 1f else 0f)
    val scaleAnimate = remember { Animatable(initialScale) }
    val offsetAnimate = remember { Animatable(initialOffset, Dp.VectorConverter) }
    val messageQueue = remember { Channel<Toast.UiEvent>(Channel.UNLIMITED) }

    LaunchedEffect(Unit) {
        Toast.EventBus.event.collect { event ->
            messageQueue.trySend(element = event)
        }
    }

    LaunchedEffect(Unit) {
        for (item in messageQueue) {
            toastState = ToastState(
                visible = true,
                type = item.type,
                message = item.message,
            )
            launch {
                scaleAnimate.animateTo(1f, animationSpec = tween(animationDuration))
            }
            launch {
                offsetAnimate.animateTo(0.dp, animationSpec = tween(animationDuration))
            }
            delay(item.duration.toLong())
            toastState = toastState.copy(visible = false)
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
        contentAlignment = alignment,
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .widthIn(120.dp, maxWidth)
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
                when (toastState.type) {
                    Toast.ToastType.Success -> {
                        QmIcon(
                            icon = QmIcons.Stroke.Success,
                            size = 22.dp,
                            tint = Color(0xFF3AC786),
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }

                    Toast.ToastType.Warning -> {
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
                    text = toastState.message,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = if (toastState.type == Toast.ToastType.Default) 2 else 1,
                )
            }
        }
    }
}
