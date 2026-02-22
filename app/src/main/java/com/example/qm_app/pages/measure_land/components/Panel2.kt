package com.example.qm_app.pages.measure_land.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.qm_app.components.PullToRefreshWidget
import com.example.qm_app.components.rememberPullToRefreshState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Panel2(event: Channel<Boolean>) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val state =
        rememberPullToRefreshState(threshold = 60.dp, maxOffset = 200.dp) { pullToRefreshState ->
            coroutineScope.launch {
                delay(2000)
                pullToRefreshState.refreshComplete()
            }
        }

    PullToRefreshWidget(lazyListState = lazyListState, state = state) {
        LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color(0xFFFF9900))
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color(0xFFFF7700))
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color(0xFFFF5500))
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color(0xFFFF3300))
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color(0xFFFF1100))
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color(0xFFFF0000))
                )
            }
        }
    }
}
