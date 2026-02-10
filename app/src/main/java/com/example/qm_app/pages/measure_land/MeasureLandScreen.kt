package com.example.qm_app.pages.measure_land

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.pages.measure_land.components.Panel1

@Composable
fun MeasureLandScreen() {
    PageScaffold(title = "测量宝") { paddingValues ->
        val pagerState = rememberPagerState(initialPage = 0) { 3 }
        Box(modifier = Modifier.padding(paddingValues)) {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                userScrollEnabled = false,
                beyondViewportPageCount = 1,
            ) {
                Panel1()
            }
        }
    }
}