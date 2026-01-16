package com.example.qm_app.pages.user_auth_farm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner2
import com.example.qm_app.ui.theme.primaryColor

@Composable
fun ModuleTitleWidget(title: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.then(Modifier.fillMaxWidth()),
    ) {
        Box(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(3.dp, 14.dp)
                .background(color = primaryColor, shape = corner2)
        )
        Text(text = title, fontSize = 14.sp, lineHeight = 14.sp, color = black4)
    }
}