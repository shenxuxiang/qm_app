package com.example.qm_app.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshColumn(
    threshold: Dp,
    modifier: Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    refreshState: PullToRefreshState = rememberPullToRefreshState(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .pullToRefresh(
                state = refreshState,
                threshold = threshold,
                onRefresh = onRefresh,
                isRefreshing = isRefreshing,
            )
            .then(modifier)

    ) {
        PullRefreshIndicator(
            threshold = threshold,
            refreshState = refreshState,
            isRefreshing = isRefreshing,
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRefreshIndicator(
    threshold: Dp,
    isRefreshing: Boolean,
    refreshState: PullToRefreshState,
) {
    val density = LocalDensity.current
    val height = remember { mutableStateOf(0.dp) }
    val thresholdToPx = remember { with(density) { threshold.toPx() } }

    LaunchedEffect(refreshState) {
        snapshotFlow { refreshState.distanceFraction }.collect {
            height.value = with(density) { (refreshState.distanceFraction * thresholdToPx).toDp() }
        }
    }

    Box(
        contentAlignment = BiasAlignment(0f, 0.5f),
        modifier = Modifier
            .fillMaxWidth()
            .height(height.value)
            .clipToBounds()
            .graphicsLayer(clip = true)
    ) {
        val context = if (isRefreshing) {
            "正在刷新"
        } else {
            if (refreshState.distanceFraction > 1) {
                "释放刷新"
            } else {
                if (!refreshState.isAnimating) {
                    "下拉刷新"
                } else {
                    ""
                }
            }
        }

        Crossfade(targetState = context, animationSpec = tween(durationMillis = 150)) {
            Text(
                text = it,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF999999),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}