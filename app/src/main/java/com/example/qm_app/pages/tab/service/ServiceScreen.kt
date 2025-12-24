package com.example.qm_app.pages.tab.service

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
import com.example.qm_app.common.QmLoadingManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ServiceScreen() {
    val context = LocalContext.current
    val favoriteViewModel = hiltViewModel<ServiceViewModel>()
    val uiState = favoriteViewModel.uiState.collectAsState()
    println(uiState.value.bannerList)
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.systemBarsPadding()) {
        Button(onClick = {
            coroutineScope.launch {
                // favoriteViewModel.getBannerList(body = emptyMap())
//                QmToastManager.showToast("自定义弹框展示自定义弹框展示自定义弹框展示自定义弹框展示")
//                delay(500)
//                QmToastManager.showSuccessToast("自定义弹框展示")
//                delay(500)
//                QmToastManager.showWarningToast("自定义弹框展示")

                QmLoadingManager.show("数据加载中...")
                delay(300)
                QmLoadingManager.hide()
            }
        }) {
            Text("发送请求")
        }
    }
}



