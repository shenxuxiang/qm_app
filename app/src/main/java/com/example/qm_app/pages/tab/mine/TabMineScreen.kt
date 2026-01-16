package com.example.qm_app.pages.tab.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.R
import com.example.qm_app.pages.tab.mine.components.HeaderWidget
import com.example.qm_app.pages.tab.mine.components.MenuBoxWidget
import com.example.qm_app.pages.tab.mine.components.UserCheckStatusWidget

@Composable
fun MineScreen() {
    val viewModel = hiltViewModel<TabMineViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.queryUserCheckStatus()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.background_img_variant),
        )
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderWidget()
            UserCheckStatusWidget(viewModel)
            MenuBoxWidget(
                title = "我的服务",
                menus = uiState.mineServiceMenus,
                modifier = Modifier.padding(12.dp)
            )
            MenuBoxWidget(
                title = "我的设置",
                menus = uiState.mineSettingsMenus,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}
