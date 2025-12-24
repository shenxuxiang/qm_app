package com.example.qm_app.pages.tab.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MineScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF5500))
    ) {
        Text("Hello World Mine Screen")
    }
}
