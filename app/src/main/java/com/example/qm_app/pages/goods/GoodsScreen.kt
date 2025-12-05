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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.qm_app.R
import com.example.qm_app.common.QmApplication
import com.example.qm_app.pages.favorite.components.GoodsCategory
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

    LaunchedEffect(Unit) {
        snapshotFlow { toolbarState.height }.collect {
            savedStateHandle["toolbarHeight"] = it
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            listOf(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }.collect {
            savedStateHandle["firstVisibleItemIndex"] = it[0]
            savedStateHandle["firstVisibleItemScrollOffset"] = it[1]
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        Image(
            painter = painterResource(R.drawable.img),
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
                    GoodsCategory()
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