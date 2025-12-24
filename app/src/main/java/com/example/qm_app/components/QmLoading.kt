package com.example.qm_app.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmLoadingManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@Stable
data class LoadingState(val visible: Boolean, val message: String)

@Composable
fun QmLoading(
    animationDuration: Int = 300,
    alignment: Alignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.5f),
) {
    val initialScale = 0.6f
    val maxWidth = (LocalConfiguration.current.screenWidthDp * 0.8).dp
    val initialOffset = with(LocalDensity.current) { -50.dp.toPx() }

    val taskQueue = remember { Channel<QmLoadingManager.UiEvent>(Channel.CONFLATED) }
    var loadingState by remember { mutableStateOf(LoadingState(message = "", visible = false)) }

    // 透明度、缩放、偏移量设置
    val scaleAnimate = remember { Animatable(initialScale) }
    val offsetAnimate = remember { Animatable(initialOffset) }
    val alpha = animateFloatAsState(targetValue = if (loadingState.visible) 1f else 0f)

    LaunchedEffect(Unit) {
        QmLoadingManager.EventBus.event.collect { taskQueue.trySend(it) }
    }

    LaunchedEffect(taskQueue) {
        for (task in taskQueue) {
            if (task is QmLoadingManager.UiEvent.ShowLoading) {
                loadingState = LoadingState(message = task.message, visible = true)
                launch {
                    scaleAnimate.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(animationDuration)
                    )
                }
                launch {
                    offsetAnimate.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(animationDuration)
                    )
                }
            } else {
                loadingState = loadingState.copy(visible = false)
            }
        }
    }

    LaunchedEffect(alpha.value) {
        if (alpha.value == 0f) {
            scaleAnimate.snapTo(initialScale)
            offsetAnimate.snapTo(initialOffset)
        }
    }

    if (alpha.value != 0f) {
        Box(
            contentAlignment = alignment,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = {}, indication = null, interactionSource = null),
        ) {
            Box(
                modifier = Modifier
                    .widthIn(0.dp, maxWidth)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .graphicsLayer(
                        alpha = alpha.value,
                        scaleY = scaleAnimate.value,
                        scaleX = scaleAnimate.value,
                        translationY = offsetAnimate.value,
                    )
                    .background(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF000000).copy(alpha = 0.7f),
                    )
                    .padding(vertical = 14.dp, horizontal = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round,
                        color = Color(0xFF3AC786),
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "数据加载中...",
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 10.dp),
                    )
                }
            }
        }
    }
}