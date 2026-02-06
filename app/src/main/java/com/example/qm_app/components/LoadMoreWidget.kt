package com.example.qm_app.components

import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.gray

/**
 * 加载状态
 * */
class LoadMoreState(initialState: LoadState) {
    enum class LoadState { None, Loading, NotMore }

    private val _state = mutableStateOf(initialState)
    val state: LoadState
        get() = _state.value

    companion object {
        val Saver = Saver<LoadMoreState, LoadState>(
            save = { it.state },
            restore = { LoadMoreState(it) }
        )
    }

    fun finishLoad(notMore: Boolean = false) {
        _state.value = if (notMore) LoadState.NotMore else LoadState.None
    }

    fun isLoading() {
        _state.value = LoadState.Loading
    }
}

@Composable
fun rememberLoadMoreState(): LoadMoreState {
    return rememberSaveable(saver = LoadMoreState.Saver) {
        LoadMoreState(LoadMoreState.LoadState.None)
    }
}

@Composable
fun LoadMoreWidget(loadState: LoadMoreState, onLoad: () -> Unit, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val onLoadProps by rememberUpdatedState(onLoad)

    LaunchedEffect(Unit) {
        if (loadState.state == LoadMoreState.LoadState.None) {
            loadState.isLoading()
            onLoadProps()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(80.dp, 0.5.dp)
                    .background(black.copy(0.2f))
            )
            Text(
                color = gray,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                modifier = Modifier.padding(end = 2.dp),
                text = if (loadState.state == LoadMoreState.LoadState.NotMore) "没有更多数据了" else "加载中",
            )
            if (loadState.state != LoadMoreState.LoadState.NotMore)
                Row {
                    Dot(transition, 0)
                    Dot(transition, 1)
                    Dot(transition, 2)
                }
            Box(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(80.dp, 0.5.dp)
                    .background(black.copy(0.2f))
            )
        }
    }
}

@Composable
fun Dot(transition: InfiniteTransition, index: Int) {
    val animate = transition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, 0, Ease),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(offsetMillis = 150 * index, StartOffsetType.Delay),
        )
    )

    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(2.dp)
            .graphicsLayer(translationY = 5f - animate.value)
            .background(gray, CircleShape)
    )
}