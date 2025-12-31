package com.example.qm_app.components.alert

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@Composable
fun AlertWidget(
    animationDuration: Int = 300,
    alignment: Alignment = BiasAlignment(0f, -0.2f),
) {
    val initialScale = 0.2f
    val initialOffset = with(LocalDensity.current) { (-150).dp.toPx() }
    val channel = remember { Channel<AlertUiEvent>(Channel.CONFLATED) }

    val showAlert = remember { mutableStateOf(false) }
    val alertEvent = remember { mutableStateOf<AlertUiEvent?>(null) }
    val alpha by animateFloatAsState(
        targetValue = if (showAlert.value) 1f else 0f,
        animationSpec = tween(durationMillis = animationDuration),
    )
    val scaleAnimate = remember { Animatable(initialScale) }
    val offsetAnimate = remember { Animatable(initialOffset) }

    LaunchedEffect(Unit) {
        Alert.EventBus.event.collect { channel.trySend(it) }
    }

    LaunchedEffect(channel) {
        for (event in channel) {
            showAlert.value = true
            alertEvent.value = event
            launch {
                scaleAnimate.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = animationDuration)
                )
            }
            launch {
                offsetAnimate.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = animationDuration)
                )
            }
        }
    }

    LaunchedEffect(alpha) {
        if (alpha == 0f) {
            scaleAnimate.snapTo(initialScale)
            offsetAnimate.snapTo(initialOffset)
        }
    }

    if (alpha > 0f) {
        Box(
            contentAlignment = alignment,
            modifier = Modifier
                .alpha(alpha)
                .fillMaxSize()
                .background(color = black.copy(alpha = 0.4f)),
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 33.dp)
                    .fillMaxWidth()
                    .graphicsLayer(
                        translationY = offsetAnimate.value,
                        scaleX = scaleAnimate.value,
                        scaleY = scaleAnimate.value,
                        alpha = alpha
                    )
                    .clip(corner10)
                    .background(color = white)
            ) {
                val content = alertEvent.value!!.content
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Body 部分
                    if (content is AlertContent.TextContent) {
                        Text(
                            text = content.message,
                            color = black4,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier
                                .padding(vertical = 28.dp, horizontal = 31.dp)
                                .fillMaxWidth(),
                        )
                    } else {
                        (content as AlertContent.ComposableContent).content
                    }
                    // Footer 部分
                    if (alertEvent.value!!.footer == null) {
                        Row(
                            modifier = Modifier.padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            if (alertEvent.value!!.showCancel)
                                ButtonWidget(
                                    type = ButtonWidgetType.Default,
                                    text = alertEvent.value!!.cancelText,
                                    modifier = Modifier
                                        .padding(horizontal = 9.dp)
                                        .size(120.dp, 36.dp),
                                    onTap = {
                                        alertEvent.value!!.onCancel?.invoke()
                                        showAlert.value = false
                                    },
                                )

                            if (alertEvent.value!!.showConfirm)
                                ButtonWidget(
                                    type = ButtonWidgetType.Primary,
                                    text = alertEvent.value!!.confirmText,
                                    modifier = Modifier
                                        .padding(horizontal = 9.dp)
                                        .size(120.dp, 36.dp),
                                    onTap = {
                                        alertEvent.value!!.onConfirm?.invoke()
                                        showAlert.value = false
                                    },
                                )
                        }
                    } else {
                        alertEvent.value!!.footer
                    }
                }
            }
        }
    }
}