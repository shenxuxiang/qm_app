package com.example.qm_app.pages.map_location.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.corner8
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.white

@Composable
fun SearchBox(placeholder: String, value: String, onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(40.dp)
            .background(white, corner8)
            .padding(horizontal = 8.dp)
            .clickable(onClick = onTap, indication = null, interactionSource = null),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QmIcon(
            size = 24.dp,
            tint = black3,
            icon = QmIcons.Stroke.Search,
            modifier = Modifier.padding(end = 6.dp)
        )
        if (value.isEmpty()) {
            Text(text = placeholder, fontSize = 14.sp, color = gray, lineHeight = 14.sp)
        } else {
            Text(
                maxLines = 1,
                color = black3,
                fontSize = 14.sp,
                text = placeholder,
                lineHeight = 14.sp,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}