package com.example.qm_app.pages.favorite.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun GoodsCategory(
    value: Int? = null,
    categories: List<String>,
    onChanged: ((value: Int) -> Unit)?,
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var tabIndex by remember { mutableIntStateOf(value = value ?: 0) }

    // 每个 Tab 的实际宽度的集合
    val tabItemWidthList = remember(categories) { MutableList(categories.size) { -1 } }
    // 容器的宽度，初始化时为 -1，待容器加载完成后就会通过 onGloballyPositioned 事件回调完成赋值
    var containerWidth = remember { -1 }
    // 容器的内容宽度
    val containerScrollWidthDp = remember { mutableStateOf(0.dp) }

    val propsValue = rememberUpdatedState(value)
    val propsOnChanged = rememberUpdatedState(onChanged)

    val indicatorAnimate = remember { Animatable(initialValue = 0, Int.VectorConverter) }

    LaunchedEffect(tabIndex) {
        if (tabIndex != propsValue.value) propsOnChanged.value?.invoke(tabIndex)
        // 当前 Tab 的宽度
        val tabWidth = tabItemWidthList[tabIndex]
        // 当前 Tab 距离容器左侧的距离，subList(start, end) 包含开始不包含结束
        var offsetLeft =
            tabItemWidthList.subList(0, tabIndex).fold(initial = 0) { sum, offset -> sum + offset }

        // 通过动画的形式将 indicator 跳转到指定偏移量
        launch { indicatorAnimate.animateTo(offsetLeft) }

        // 注意，这两的 scrollState 控制着两个 Row 的滚动容器，
        // 所以，当我们调用 scrollState.animateScrollTo() 后，两个 Row 就都会发生滚动。
        launch {
            // scrollState.maxValue 表示当前 Row 滚动容器最大的滚动偏移量
            offsetLeft = (offsetLeft - containerWidth / 2 + tabWidth / 2).coerceIn(
                0,
                scrollState.maxValue
            )
            scrollState.animateScrollTo(offsetLeft)
        }
    }

    LaunchedEffect(value) {
        if (value != null && value != tabIndex) tabIndex = value
    }

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 50.dp)
                .horizontalScroll(scrollState)
                .onGloballyPositioned { coordinates ->
                    if (containerWidth == -1 && coordinates.parentCoordinates?.size?.width != null) {
                        containerWidth = coordinates.parentCoordinates!!.size.width
                    }

                    if (containerScrollWidthDp.value == 0.dp) {
                        containerScrollWidthDp.value =
                            with(density) { coordinates.size.width.toDp() }
                    }
                }
        ) {
            categories.forEachIndexed { index, tab ->
                val isSelected = tabIndex == index
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(height = 50.dp)
                        .padding(horizontal = 16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = {
                                if (tabIndex == index) return@clickable

                                tabIndex = index
                            },
                        )
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            if (tabItemWidthList[index] == -1) {
                                tabItemWidthList[index] = placeable.width + 32.dp.roundToPx()
                            }

                            layout(placeable.width, placeable.height) {
                                placeable.placeRelative(0, 0)
                            }
                        },
                ) {
                    Text(
                        text = tab,
                        fontSize = 14.sp,
                        color = if (isSelected) Color(0xFFFF9900) else Color(0xFF666666)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .align(Alignment.BottomStart)
        ) {
            Box(
                modifier = Modifier
                    .size(containerScrollWidthDp.value, 4.dp)
                    .offset { IntOffset(x = indicatorAnimate.value, y = 0) }
            ) {
                Box(
                    modifier = Modifier
                        .size(with(density) { tabItemWidthList[tabIndex].toDp() }, 4.dp)
                        .padding(horizontal = 16.dp)
                        .background(color = Color(0xFFFF9900), shape = RoundedCornerShape(2.dp))
                )
            }
        }
    }
}


