package com.example.qm_app.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.ui.theme.QmIconFontFamily

@Composable
fun QmIcon(
    icon: QmIcons,
    size: Dp = 16.dp,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
) {
    Text(
        color = tint,
        text = icon.icon,
        modifier = modifier,
        fontSize = size.value.sp,
        lineHeight = size.value.sp,
        fontFamily = QmIconFontFamily,
    )
}