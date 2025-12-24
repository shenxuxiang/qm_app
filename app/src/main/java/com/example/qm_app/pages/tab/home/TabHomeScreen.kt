package com.example.qm_app.pages.tab.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.components.Swiper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabHomeScreen() {
    PageScaffold(title = "首页") { paddingValues ->
        val tabHomeViewModel = hiltViewModel<TabHomeViewModel>()
        val uiState by tabHomeViewModel.uiState.collectAsState()

        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = uiState.firstVisibleItemIndex,
            initialFirstVisibleItemScrollOffset = uiState.firstVisibleItemScrollOffset,
        )

        DisposableEffect(Unit) {
            onDispose {
                tabHomeViewModel.saveScrollState(
                    listState.firstVisibleItemIndex,
                    listState.firstVisibleItemScrollOffset
                )
            }
        }

        LazyColumn(state = listState, modifier = Modifier.padding(paddingValues)) {
            item {
                Swiper(
                    index = uiState.bannerIndex,
                    height = 153.dp,
                    options = uiState.bannerList,
                    onChanged = { tabHomeViewModel.updateBannerIndex(it) },
                )
            }
        }
    }
}
