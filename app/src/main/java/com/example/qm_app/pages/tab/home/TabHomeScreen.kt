package com.example.qm_app.pages.tab.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.R
import com.example.qm_app.components.MenuItemOption
import com.example.qm_app.components.MenuListBox
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.components.PullToRefreshColumn
import com.example.qm_app.components.Swiper
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.pages.tab.home.components.NewsItemBox
import kotlinx.coroutines.launch

val menuOptions = listOf(
    MenuItemOption(label = "测量宝", res = R.drawable.home_1),
    MenuItemOption(label = "农技作业", res = R.drawable.home_2),
    MenuItemOption(label = "病虫害识别", res = R.drawable.home_3),
    MenuItemOption(label = "我的农场", res = R.drawable.home_4),
    MenuItemOption(label = "找农资", res = R.drawable.home_5),
    MenuItemOption(label = "找农机", res = R.drawable.home_6),
    MenuItemOption(label = "找植保", res = R.drawable.home_7),
    MenuItemOption(label = "找农技", res = R.drawable.home_8),
    MenuItemOption(label = "找贷款", res = R.drawable.home_9),
    MenuItemOption(label = "找保险", res = R.drawable.home_10),
    MenuItemOption(label = "找政策", res = R.drawable.home_11),
    MenuItemOption(label = "找专家", res = R.drawable.home_12),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabHomeScreen() {
    PageScaffold(title = "首页") { paddingValues ->
        val tabHomeViewModel = hiltViewModel<TabHomeViewModel>()
        val uiState by tabHomeViewModel.uiState.collectAsState()

        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = 0, // uiState.firstVisibleItemIndex,
            initialFirstVisibleItemScrollOffset = 0, // uiState.firstVisibleItemScrollOffset,
        )

        val coroutineScope = rememberCoroutineScope()
        PullToRefreshColumn(
            threshold = 60.dp,
            modifier = Modifier.padding(paddingValues),
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                coroutineScope.launch {
                    Loading.show()
                    tabHomeViewModel.handleRefresh()
                    Loading.hide()
                }
            },
        ) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                item {
                    Swiper(
                        index = uiState.bannerIndex,
                        height = 153.dp,
                        options = uiState.bannerList,
                        onChange = {
                            tabHomeViewModel.updateBannerIndex(it)
                        },
                    )
                }
                item {
                    MenuListBox(options = menuOptions)
                }
                itemsIndexed(items = uiState.newsList) { _, news ->
                    NewsItemBox(news)
                }
            }
        }
    }
}
