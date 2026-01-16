package com.example.qm_app.pages.user_auth_select

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.white

@Composable
fun IDCardWidget(
    icon: Painter,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.then(
            Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .background(color = white, shape = corner10)
                .padding(vertical = 18.dp, horizontal = 12.dp)
                .clickable(onClick = { onTap() }, indication = null, interactionSource = null),
        )
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(66.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                color = black3,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = subtitle,
                color = black4,
                fontSize = 13.sp,
                lineHeight = 13.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        QmIcon(icon = QmIcons.Stroke.Forward, tint = gray, size = 24.dp)
    }
}