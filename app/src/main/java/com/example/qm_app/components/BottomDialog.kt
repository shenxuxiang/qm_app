package com.example.qm_app.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.white

@Composable
fun BottomDialog(
    open: Boolean,
    content: @Composable () -> Unit,
) {
    val transitionY = animateDpAsState(
        animationSpec = tween(300),
        targetValue = if (open) 0.dp else (560).dp,
    )

    if (transitionY.value < 560.dp)
        Popup(
            alignment = Alignment.TopStart,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = false,
                dismissOnClickOutside = true,
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.BottomStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(560.dp)
                        .offset(x = 0.dp, y = transitionY.value)
                        .background(color = white)
                ) {
                    content()
                }
            }
        }
}