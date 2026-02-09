package com.example.qm_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.qm_app.modifier.boxShadow
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.white

@Composable
fun PageFootButton(text: String, disabled: Boolean = false, onTap: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .boxShadow(x = 0.dp, y = 2.dp, blur = 4.dp, color = black.copy(alpha = 0.2f))
            .background(white)
    ) {
        ButtonWidget(
            text = text,
            onTap = { onTap() },
            disabled = disabled,
            type = ButtonWidgetType.Primary,
            modifier = Modifier
                .padding(horizontal = 46.dp)
                .fillMaxWidth()
                .height(36.dp),
        )
    }
}