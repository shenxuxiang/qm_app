package com.example.qm_app.pages.camera

import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.gray

@Composable
fun Header(
    flashMode: Int,
    modifier: Modifier,
    onChangeFlashMode: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .background(color = black)
                .padding(vertical = 40.dp, horizontal = 40.dp)
        ),
    ) {
        /* 打开闪光灯、关闭闪光灯 */
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .border(
                    width = 2.dp,
                    shape = CircleShape,
                    color = if (flashMode == FLASH_MODE_ON) Color(0xFFFF9900) else gray,
                )
                .clickable(onClick = {
                    onChangeFlashMode()
                }),
        ) {
            QmIcon(
                size = 26.dp,
                icon = QmIcons.Stroke.FlashLight,
                tint = if (flashMode == FLASH_MODE_ON) Color(0xFFFF9900) else gray,
            )
        }
    }
}