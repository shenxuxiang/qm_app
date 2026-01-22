package com.example.qm_app.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinx.coroutines.launch

@Composable
fun Swiper(
    height: Dp,
    index: Int = 0,
    options: List<String>,
    onChange: (index: Int) -> Unit,
    content: (@Composable (PagerScope.(Int, option: String) -> Unit))? = null,
) {
    val context = LocalContext.current
    val interval = remember { mutableStateOf(0) }
    val state = rememberPagerState(initialPage = index, pageCount = { options.size })

    LaunchedEffect(Unit) {
        snapshotFlow { state.currentPage }.collect {
            onChange(it)
            launch {
                delay(300)
                interval.value++
            }
        }
    }

    LaunchedEffect(index) {
        if (index != state.currentPage) state.scrollToPage(index)
    }

    LaunchedEffect(interval.value) {
        while (true) {
            delay(5000)
            val idx = if (state.currentPage >= state.pageCount - 1) 0 else state.currentPage + 1

            state.animateScrollToPage(idx, animationSpec = tween(300))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
    ) {
        HorizontalPager(
            state,
            pageSize = PageSize.Fill,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = options.size,
        ) { it ->
            if (content == null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(getNetworkAssetURL(options[it]))
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

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .offset(x = 0.dp, y = (-12).dp)
        ) {
            repeat(options.size) {
                IndicatorItem(active = it == state.currentPage)
            }
        }
    }
}

@Composable
private fun IndicatorItem(active: Boolean) {
    val indicatorWidth = animateDpAsState(
        targetValue = if (active) 12.dp else 6.dp,
        animationSpec = tween(durationMillis = 500),
    )
    val backgroundColor = animateColorAsState(
        targetValue = if (active) primaryColor else white,
        animationSpec = tween(durationMillis = 300),
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .size(width = indicatorWidth.value, height = 6.dp)
            .clip(shape = RoundedCornerShape(size = 3.dp))
            .background(color = backgroundColor.value)
    )
}