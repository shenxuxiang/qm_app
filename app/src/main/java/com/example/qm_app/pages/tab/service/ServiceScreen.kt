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
import com.example.qm_app.components.loading.LoadingManager
import com.example.qm_app.components.toast.ToastManager
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
//                delay(500)
                ToastManager.showSuccessToast("自定义弹框展示")
                delay(500)
//                QmToastManager.showWarningToast("自定义弹框展示")

                LoadingManager.show("数据加载中...")
                delay(3000)
                LoadingManager.hide()
            }
        }) {
            Text("发送请求")
        }
    }
}



