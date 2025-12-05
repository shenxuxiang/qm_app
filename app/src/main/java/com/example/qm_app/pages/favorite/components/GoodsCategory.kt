package com.example.qm_app.pages.favorite.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GoodsCategory() {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableIntStateOf(value = 0) }
    val categories = listOf("推荐", "科技", "体育", "娱乐", "财经", "汽车", "房产", "教育")

    val offsetList = remember { MutableList(categories.size) { -1 } }
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.roundToPx() }

    LaunchedEffect(selectedTab) {
        val width = offsetList[selectedTab]
        var offsetX = offsetList.subList(0, selectedTab).fold(0) { s, n -> s + n }
        println("offsetX: $offsetX")
        offsetX = (offsetX - screenWidthPx / 2 + width / 2).coerceIn(
            0,
            scrollState.maxValue
        )
        scrollState.animateScrollTo(offsetX)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
            .horizontalScroll(scrollState)
    ) {
        categories.forEachIndexed { index, tab ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(50.dp)
                    .background(Color(0xFFCCCCCC))
                    .padding(horizontal = 16.dp)
                    .clickable(onClick = { selectedTab = index })
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        if (offsetList[index] == -1) {
                            offsetList[index] = placeable.width + 32.dp.roundToPx()
                        }

                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    },
            ) {
                Text(text = tab, fontSize = 14.sp, color = Color(0xFF666666))
            }
        }
    }
}

@Composable
fun MeasurableRow(categories: List<String>) {
    val scrollState = rememberScrollState()
    SubcomposeLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .horizontalScroll(scrollState)
    ) { constraints ->
        // 预测量所有子项
        val measurables = categories.map { category ->
            subcompose(category) {
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                        .background(Color(0xFFCCCCCC)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = category, fontSize = 14.sp)
                }
            }.first()
        }

        // 测量所有子项
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        // 获取尺寸信息
        val sizes = placeables.map { IntSize(it.width, it.height) }
        for (size in sizes) {
            println("size: ${size.width}, ${size.height}")
        }
        val containerWidth = sizes.fold(0) { sum, size ->
            sum + size.width
        }

        println("constraints.maxWidth: ${constraints.maxWidth}")
        // 布局逻辑（保持原Row行为）
        layout(containerWidth, 60.dp.roundToPx()) {
            var xPos = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(xPos, 0)
                xPos += placeable.width
            }
        }
    }
}
