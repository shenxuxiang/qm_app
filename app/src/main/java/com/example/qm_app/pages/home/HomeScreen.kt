package com.example.qm_app.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, paddingValues: PaddingValues) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "首页", fontSize = 18.sp, color = Color.White)
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF9900),
                    scrolledContainerColor = Color(0xFFFF9900)
                ),
            )
        }
    ) { innerPadding ->

        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = homeViewModel.scrollState.value.firstVisibleItemIndex,
            initialFirstVisibleItemScrollOffset = homeViewModel.scrollState.value.firstVisibleItemScrollOffset,
        )

        // 实时监听 firstVisibleItemIndex && firstVisibleItemScrollOffset 变化
        LaunchedEffect(listState) {
            snapshotFlow {
                // 如果只监听一个数据，则直接返回该数据即可，如果是多个数据，使用 listOf
                listOf(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
            }.collect { resp ->
                homeViewModel.updateScrollState(itemIndex = resp[0], offset = resp[1])
            }
        }

        LazyColumn(state = listState, modifier = Modifier.padding(innerPadding)) {
            items(30) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(
                            top = if (index == 0) 12.dp else 0.dp,
                            start = 12.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                        .clickable(onClick = {
                            if (index == 11) {
                                navController.navigate("user?id=12345")
                            }
                        })
                ) {
                    Text("Hello World Index: $index")
                }
            }
        }
    }
}