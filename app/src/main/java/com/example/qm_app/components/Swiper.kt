package com.example.qm_app.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun rememberSwiperState(
    count: Int,
    initialIndex: Int,
    onChanged: (index: Int) -> Unit,
): PagerState {
    val state = rememberPagerState(initialPage = initialIndex, pageCount = { count })

    LaunchedEffect(state) {
        snapshotFlow { state.currentPage }.collect {
            onChanged(it)
        }
    }

    return state
}

@Composable
fun Swiper(
    height: Dp,
    index: Int = 0,
    options: List<String>,
    onChanged: (index: Int) -> Unit,
    content: (@Composable (PagerScope.(Int, option: String) -> Unit))? = null,
) {
    val context = LocalContext.current
    val currentIndex = rememberUpdatedState(index)
    val state = rememberPagerState(initialPage = index, pageCount = { options.size })

    LaunchedEffect(state) {
        snapshotFlow { state.currentPage }.collect {
            if (currentIndex.value != it) onChanged(it)
        }
    }

    LaunchedEffect(index) {
        if (index != state.currentPage) state.scrollToPage(index)
    }

    println("options: ${options.size}")

    HorizontalPager(
        state,
        pageSize = PageSize.Fill,
        modifier = Modifier.height(height),
        beyondViewportPageCount = options.size,
    ) { it ->
        if (content == null) {
            println(options[it])
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("http://60.169.69.3:30062${options[it]}")
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            content(it, options[it])
        }
    }
}