package com.example.qm_app.pages.user_auth_farm_view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4

@Composable
fun FormItem(label: String, value: String, bordered: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(if (bordered) 49.dp else 48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = black4,
                fontSize = 14.sp,
                lineHeight = 14.sp,
            )
            Text(
                text = value,
                color = black3,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
        if (bordered)
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFE0E0E0)
            )
    }
}
