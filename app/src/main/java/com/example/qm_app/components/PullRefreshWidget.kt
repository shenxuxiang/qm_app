package com.example.qm_app.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.ui.theme.gray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PullToRefreshState(
    val threshold: Float,
    val maxOffset: Float,
    val onRefresh: (PullToRefreshState) -> Unit
) {
    companion object {
        // 阻尼系数
        private const val RESISTANCE_FACTOR = 0.4f
    }

    private val coroutineScope = CoroutineScope(AndroidUiDispatcher.Main + SupervisorJob())
    private val animate = Animatable(0f)
    val offset by derivedStateOf { animate.value }
    val progress by derivedStateOf { (offset / threshold).coerceIn(0f, 1f) }
    var isDragging by mutableStateOf(false)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    /**
     * 用户拖拽
     * */
    fun onPull(delta: Float): Float {
        /**
         * 在刷新过程中，如果用户向上推，则直接返回 delta，
         * 也就是说，用户滑动行为不再传递给子组件，所以子组件不会滚动。
         * */
        if (isRefreshing) return if (delta < 0) delta else 0f

        if (!isDragging) isDragging = true

        val newValue = if (delta > 0) {
            val remaining = maxOffset - offset

            /**
             * 计算阻尼因子：剩余空间越大，因子越接近 1；剩余空间越小，因子越小
             * 使用 (剩余空间) / (剩余空间 + 阻尼阈值) 的公式，
             * 阻尼阈值 = maxOffset * resistanceFactor
             * */
            val factor = if (remaining > 0) {
                (remaining / (remaining + maxOffset * RESISTANCE_FACTOR)).coerceIn(0f, 1f)
            } else {
                0f
            }
            // 实际生效的下拉距离
            val actualPull = delta * factor
            (offset + actualPull).coerceAtMost(maxOffset)
        } else {
            (offset + delta).coerceIn(0f, maxOffset)
        }

        val consumed = newValue - offset
        coroutineScope.launch {
            animate.snapTo(newValue)
        }
        return consumed
    }

    /**
     * 用户释放手指
     * */
    fun onRelease() {
        if (isRefreshing) return

        isDragging = false
        if (offset >= threshold) {
            isRefreshing = true

            coroutineScope.launch {
                animate.animateTo(targetValue = threshold)
                onRefresh(this@PullToRefreshState)
            }
        } else {
            coroutineScope.launch {
                animate.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(300)
                )
            }
        }
    }

    /**
     * 完成刷新
     * */
    fun refreshComplete() {
        isRefreshing = false
        coroutineScope.launch {
            animate.animateTo(
                targetValue = 0f,
                animationSpec = tween(500)
            )
        }
    }

    /**
     * 手动触发刷新
     * */
    fun refresh() {
        if (isRefreshing) return
        isRefreshing = true

        coroutineScope.launch {
            animate.animateTo(
                targetValue = threshold,
                animationSpec = tween(300)
            )
            onRefresh(this@PullToRefreshState)
        }
    }
}

@Composable
fun rememberPullToRefreshState(
    threshold: Dp,
    maxOffset: Dp,
    onRefresh: (PullToRefreshState) -> Unit
): PullToRefreshState {
    val density = LocalDensity.current
    val thresholdPx = with(density) { threshold.toPx() }
    val maxOffsetPx = with(density) { maxOffset.toPx() }
    return remember(thresholdPx, maxOffsetPx, onRefresh) {
        PullToRefreshState(thresholdPx, maxOffsetPx, onRefresh)
    }
}

@Composable
fun PullToRefreshWidget(
    state: PullToRefreshState,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val transition = rememberInfiniteTransition()
    // 是否已经在顶部了
    val isAtTop by remember { derivedStateOf { !lazyListState.canScrollBackward } }

    val scrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 向下滑动时大于 0，向上滑动时小于 0
                val delta = available.y
                val consumed = if (delta > 0) {
                    if (isAtTop) state.onPull(delta) else 0f
                } else {
                    if (state.offset > 0) state.onPull(delta) else 0f
                }

                return Offset(0f, consumed)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                // 释放手势时检查是否触发刷新
                state.onRelease()
                return super.onPostFling(consumed, available)
            }
        }
    }

    val displayText by remember {
        derivedStateOf {
            if (state.isRefreshing) {
                "正在加载"
            } else {
                if (state.isDragging) {
                    if (state.offset >= state.threshold) {
                        "释放即可加载"
                    } else {
                        "下拉加载"
                    }
                } else {
                    ""
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .clipToBounds()
                .nestedScroll(scrollConnection),
        )
    ) {
        Box(
            modifier = Modifier
                .padding(top = with(density) { state.offset.toDp() })
                .fillMaxSize()
        ) {
            content()
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = with(density) { (-state.threshold + state.offset).toDp() })
                .fillMaxWidth()
                .height(with(density) { state.threshold.toDp() })
        ) {
            if (state.isRefreshing) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = displayText, fontSize = 14.sp, color = gray, lineHeight = 14.sp)
                    Row(modifier = Modifier.padding(start = 2.dp)) {
                        Dot(transition, 0)
                        Dot(transition, 1)
                        Dot(transition, 2)
                    }
                }
            } else {
                Text(text = displayText, fontSize = 14.sp, color = gray, lineHeight = 14.sp)
            }
        }
    }
}


