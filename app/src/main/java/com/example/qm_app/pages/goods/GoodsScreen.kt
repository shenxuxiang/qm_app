package com.example.qm_app.pages.goods

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.qm_app.R
import com.example.qm_app.common.QmApplication
import com.example.qm_app.components.GoodsCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.onebone.toolbar.rememberCollapsingToolbarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsScreen() {
    val commonViewModel = QmApplication.commonViewModel
    val savedStateHandle = QmApplication.navController.currentBackStackEntry!!.savedStateHandle

    val toolbarState =
        rememberCollapsingToolbarState(initial = savedStateHandle["toolbarHeight"] ?: Int.MAX_VALUE)
    val scaffoldState = rememberCollapsingToolbarScaffoldState(toolbarState)
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = savedStateHandle["initialFirstVisibleItemIndex"] ?: 0,
        initialFirstVisibleItemScrollOffset = savedStateHandle["initialFirstVisibleItemScrollOffset"]
            ?: 0
    )

    val categories =
        listOf("推荐", "科技", "体育", "娱乐", "财经", "汽车", "房产", "教育")
    val tabIndex = remember { mutableIntStateOf(savedStateHandle["tabIndex"] ?: 0) }

    DisposableEffect(Unit) {
        onDispose {
            savedStateHandle["tabIndex"] = tabIndex.value
            savedStateHandle["toolbarHeight"] = toolbarState.height
            savedStateHandle["firstVisibleItemIndex"] = listState.firstVisibleItemIndex
            savedStateHandle["firstVisibleItemScrollOffset"] =
                listState.firstVisibleItemScrollOffset
        }
    }

    val isRefreshing = remember { mutableStateOf(false) }
    val refreshState = remember { PullToRefreshState() }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                state = refreshState,
                onRefresh = {
                    isRefreshing.value = true
                    coroutineScope.launch {
                        delay(2000)
                        isRefreshing.value = false
                    }
                },
                isRefreshing = isRefreshing.value,
            ),
        contentAlignment = Alignment.TopStart,
    ) {
        Image(
            painter = painterResource(R.drawable.background_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        CollapsingToolbarScaffold(
            state = scaffoldState,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .parallax(ratio = 0.2f)
                        .alpha(if (scaffoldState.toolbarState.progress >= 0.7) 1f else scaffoldState.toolbarState.progress / 0.7f),
                    painter = painterResource(R.drawable.image1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .road(
                            whenCollapsed = Alignment.TopStart,
                            whenExpanded = Alignment.TopStart
                        )
                        .statusBarsPadding()
                        .height(60.dp)
                        .alpha(if (scaffoldState.toolbarState.progress >= 0.7) 0f else 1 - scaffoldState.toolbarState.progress / 0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    GoodsCategory(
                        categories = categories,
                        value = tabIndex.value,
                        onChanged = { tabIndex.value = it })
                }
            }
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(100) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(
                                top = if (index == 0) 12.dp else 0.dp,
                                start = 12.dp,
                                end = 12.dp,
                                bottom = 12.dp
                            )
                            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = Color(0xFFD9D9D9),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .clickable(onClick = {
                                if (index == 5) {
                                    commonViewModel.navToMainScreen("home")
                                }
                            })
                    ) {
                        Text("Hello World Index: $index")
                    }
                }
            }
        }
    }
}