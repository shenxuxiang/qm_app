package com.example.qm_app.pages.map_location.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amap.api.services.core.PoiItemV2
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black6

@Composable
fun DisplayPoiItemWidget(
    data: PoiItemV2,
    bordered: Boolean,
    isSelected: Boolean,
    onTap: (PoiItemV2) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (bordered) {
                    drawLine(
                        strokeWidth = 0.5.dp.toPx(),
                        color = Color(0xFFD0D0D0),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                    )
                }
            }
            .clickable(onClick = { onTap(data) })
            .padding(vertical = 15.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                color = black3,
                fontSize = 16.sp,
                text = data.title,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                color = black6,
                fontSize = 13.sp,
                text = data.snippet,
                lineHeight = 13.sp,
                modifier = Modifier.padding(top = 7.dp)
            )
        }
        Crossfade(targetState = isSelected, animationSpec = tween(300)) {
            if (it)
                Box(modifier = Modifier.size(22.dp)) {
                    QmIcon(
                        size = 22.dp,
                        icon = QmIcons.Stroke.Success,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
        }
    }
}