package com.example.qm_app.pages.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.common.EventBus
import com.example.qm_app.common.UiEvent
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen() {
    val context = LocalContext.current
    val favoriteViewModel = hiltViewModel<FavoriteViewModel>()
    val uiState = favoriteViewModel.uiState.collectAsState()
    println(uiState.value.bannerList)
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.systemBarsPadding()) {
        Button(onClick = {
            coroutineScope.launch {
                // favoriteViewModel.getBannerList(body = emptyMap())
                EventBus.emitEvent(UiEvent.ShowToast("自定义弹框展示"))
            }
        }) {
            Text("发送请求")
        }
    }
}



