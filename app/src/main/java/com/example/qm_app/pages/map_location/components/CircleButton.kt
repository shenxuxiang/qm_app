package com.example.qm_app.pages.map_location.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.ui.theme.black3

@Composable
fun CircleButton(modifier: Modifier, icon: QmIcons, onTap: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.then(
            Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(color = Color(0xA0FFFFFF), CircleShape)
                .clickable(onClick = onTap),
        )
    ) {
        QmIcon(icon = icon, tint = black3, size = 22.dp)
    }
}

