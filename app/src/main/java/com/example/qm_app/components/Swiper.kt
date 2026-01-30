package com.example.qm_app.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white
import com.example.qm_app.utils.getNetworkAssetURL
import kotlinx.coroutines.delay
import kotlin.math.abs

/**
 * 根据 PagerState.currentPage 计算当前指针指示器（Indicator）对应的 index 的值
 * 另外，Indicator 其实是对应 HorizontalPager 中 [D, A, B, C, D, A] 中的 1 至 4 位置上的内容
 * 那么这里的 thresholdRange 参数就是这个意图。
 * */
private fun computedIndicatorIndex(currentPage: Int, thresholdRange: IntRange): Int {
    return if (currentPage in thresholdRange) {
        currentPage - 1
    } else {
        if (currentPage == 0) {
            // 此时 Indicator 应该对应到 [D, A, B, C, （D）, A] 中的（D）
            // 对应到索引下标，所以还需要减 1
            thresholdRange.last - 1
        } else {
            // 此时 Indicator 应该对应到 [D, （A）, B, C, D, A] 中的（A）
            // 对应到索引下标，所以还需要减 1
            thresholdRange.first - 1
        }
    }
}

/**
 * 更新 Pager 位置，确保 Swiper 始终可以无限拖拽
 * 由于 HorizontalPager 的布局为 [（D）, A, B, C, D, （A）]
 * 如果此时 Pager 处于（A） 时，应该立即跳转到第一个 A 处；
 * 如果此时 Pager 处于（D） 时，应该立即跳转到最后一个 D 处；
 * 其他情况不需要更新 currentPage
 * */
private suspend fun updateCurrentPager(pagerState: PagerState, thresholdRange: IntRange) {
    var index = pagerState.currentPage
    if (index > thresholdRange.last) {
        index = thresholdRange.first
        pagerState.scrollToPage(index)
    } else if (index < thresholdRange.first) {
        index = thresholdRange.last
        pagerState.scrollToPage(index)
    }
}

@Composable
fun Swiper(
    height: Dp,
    index: Int = 0,
    options: List<String>,
    interval: Long = 3000L,
    onChange: (index: Int) -> Unit,
    content: (@Composable (PagerScope.(Int, option: String) -> Unit))? = null,
) {
    // 滑动动画时长
    val animationDuration = 400
    val context = LocalContext.current
    // 是否是用户拖动行为
    val isUserDrag = remember { mutableStateOf(false) }
    // 设定一个阈值的有效范围，用来计算真实的 index
    val thresholdRange by derivedStateOf { IntRange(1, options.size) }
    val pagerState = rememberPagerState(initialPage = index + 1, pageCount = { options.size + 2 })
    // 用户是否正在拖拽
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    val indexProps by rememberUpdatedState(index)

    /**
     * 展示的 Pager 集合
     * 如果传入的 options 为 [A, B, C, D] 那么实际 HorizontalPager 中展示的应该是 [D, A, B, C, D, A]
     * 通过这种方式实现无限滚动
     * */
    val displayPagerList by derivedStateOf {
        if (options.isEmpty()) {
            emptyList()
        } else {
            val length = options.size + 2
            List(length) { idx ->
                when (idx) {
                    0 -> options.last()
                    length - 1 -> options.first()
                    else -> options[idx - 1]
                }
            }
        }
    }

    /**
     * 如果 index props 发生了变化，是否更新到对应的 pager
     * */
    LaunchedEffect(index) {
        // Indicator 中此时被选中 index 应该使用和 Composable 函数中 index 参数始终一致
        val value = computedIndicatorIndex(pagerState.currentPage, thresholdRange)

        if (index != value) pagerState.scrollToPage(index + 1)
    }

    /**
     * 如果 currentPage 发生了变化，立即触发 onChange
     * */
    LaunchedEffect(pagerState.currentPage) {
        val newIndex = computedIndicatorIndex(pagerState.currentPage, thresholdRange)
        if (indexProps != newIndex) onChange(newIndex)
    }

    // 当用户拖拽结束时，监听 HorizontalPager 的滚动，当滚动结束时重新计算 updateCurrentPage
    LaunchedEffect(isDragged) {
        if (isDragged) {
            isUserDrag.value = true
        } else {
            if (isUserDrag.value) {
                snapshotFlow { pagerState.currentPageOffsetFraction }.collect { fraction ->
                    if (fraction == 0f) {
                        updateCurrentPager(pagerState, thresholdRange)
                    }
                }
            }
            isUserDrag.value = false
        }
    }

    /**
     * 启动定时轮播器，duration 表示轮播的时间间隔
     * 每当用户拖拽时，取消定时器
     * */
    LaunchedEffect(isDragged, options.size) {
        while (!isDragged && options.size > 1) {
            delay(interval)
            val currentPage = pagerState.currentPage
            val index = if (currentPage > thresholdRange.last) 1 else currentPage + 1
            pagerState.animateScrollToPage(
                page = index,
                animationSpec = tween(animationDuration)
            )
            updateCurrentPager(pagerState, thresholdRange)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
    ) {
        HorizontalPager(
            pagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = options.size > 1,
            beyondViewportPageCount = options.size,
        ) {
            if (content == null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(if (displayPagerList.isNotEmpty()) getNetworkAssetURL(displayPagerList[it]) else "")
                        .allowHardware(false) // 禁用硬件位图
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                content(it, options[it])
            }
        }

        if (options.size > 1)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .offset(x = 0.dp, y = (-12).dp)
            ) {
                repeat(options.size) { index ->
                    Indicator(
                        tabIndex = index,
                        threshold = options.size,
                        currentIndex = pagerState.currentPage,
                        fraction = pagerState.currentPageOffsetFraction,
                        thresholdRange = thresholdRange,
                    )
                }
            }
    }
}

/**
 * 指针指示器
 * @param threshold 阈值，将 currentIndex 自动转换到一个有效的范围
 * */
@Composable
fun Indicator(
    threshold: Int,
    tabIndex: Int,
    currentIndex: Int,
    fraction: Float,
    thresholdRange: IntRange,
) {
    /**
     * Swiper 是一个可以无限滚动的 Banner。
     * 所以，需要将 currentIndex 转换到一个有效范围内 [0, threshold - 1]
     * 当 currentIndex == 0，则对应最后一个指针
     * 当 currentIndex > threshold，则对应第一个指针
     * 其他情况下，则对应第 currentIndex - 1 个指针
     * */
    val currentIdx by derivedStateOf {
        computedIndicatorIndex(currentIndex, thresholdRange)
    }

    // 指针指示器的宽度缩放比例
    var ratio by remember {
        mutableStateOf(if (tabIndex == currentIdx) 1f else 0f)
    }

    // 指针指示器对应的颜色
    var color by remember {
        mutableStateOf(if (tabIndex == currentIdx) primaryColor else white)
    }

    val widthAnimate = remember {
        Animatable(
            if (tabIndex == currentIdx) 16.dp else 6.dp,
            Dp.VectorConverter
        )
    }

    LaunchedEffect(Unit) {
        snapshotFlow { ratio }.collect {
            color = lerp(white, primaryColor, it)
            widthAnimate.snapTo(targetValue = (10 * it + 6).dp)
        }
    }

    /**
     * 重组时重新计算 ratio，
     * fraction > 0 说明是从右往左拖拽，fraction < 0 说明是从左往右拖拽
     * 此时，我们需要考虑 currentIdx 是不是最后一个（或第一个）指针指示器
     * */
    SideEffect {
        if (currentIdx == tabIndex) {
            ratio = 1f - abs(fraction)
        } else if (currentIdx == 0) {
            if (fraction > 0) {
                if (tabIndex == 1) {
                    ratio = 0f + abs(fraction)
                }
            } else {
                if (tabIndex == thresholdRange.last - 1) {
                    ratio = 0f + abs(fraction)
                }
            }
        } else if (currentIdx == thresholdRange.last - 1) {
            if (fraction > 0) {
                if (tabIndex == 0) {
                    ratio = 0f + abs(fraction)
                }
            } else {
                if (tabIndex == currentIdx - 1) {
                    ratio = 0f + abs(fraction)
                }
            }
        } else {
            if (tabIndex == currentIdx - 1) {
                if (fraction < 0) {
                    ratio = 0f + abs(fraction)
                }
            } else if (tabIndex == currentIdx + 1) {
                if (fraction > 0) {
                    ratio = 0f + abs(fraction)
                }
            } else {
                ratio = 0f
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .size(width = widthAnimate.value, height = 6.dp)
            .clip(shape = RoundedCornerShape(size = 3.dp))
            .background(color = color)
    )
}