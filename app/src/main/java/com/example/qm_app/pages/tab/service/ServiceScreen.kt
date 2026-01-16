package com.example.qm_app.pages.tab.service

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router

@Composable
fun ServiceScreen() {
    val context = LocalContext.current
    val favoriteViewModel = hiltViewModel<ServiceViewModel>()
    val uiState = favoriteViewModel.uiState.collectAsState()
    println(uiState.value.bannerList)
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Button(onClick = {
            Router.navigate(Route.UserAuthSelectScreen.route) {

            }
        }) {
            Text("发送请求")
        }
    }
}



