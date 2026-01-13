package com.example.qm_app.components.region_selector_modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white

/**
 * 加载中。。。
 * */
@Composable
fun LoadingWidget(isShow: Boolean) {
    if (isShow)
        Box(
            contentAlignment = BiasAlignment(0f, -0.2f),
            modifier = Modifier
                .fillMaxSize()
                .background(color = white.copy(0.6f), shape = corner10)
                .clickable(indication = null, interactionSource = null, onClick = {}),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = primaryColor,
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "数据加载中···",
                    fontSize = 14.sp,
                    color = gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
}