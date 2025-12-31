package com.example.qm_app.pages.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.ui.theme.black4

@Composable
fun DividerWidget(label: String) {
    Row(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp, 0.5.dp)
                .background(color = Color(0xFFCCCCCC))
        )
        Text(
            text = label,
            color = black4,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 6.dp),
        )
        Box(
            modifier = Modifier
                .size(20.dp, 0.5.dp)
                .background(color = Color(0xFFCCCCCC))
        )
    }
}