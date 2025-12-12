package com.example.qm_app.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun QmCheckbox(
    size: Dp = 30.dp,
    radius: Dp = 6.dp,
    duration: Int = 300,
    value: Boolean = false,
    onChanged: (value: Boolean) -> Unit,
) {
    val checked = remember { mutableStateOf(value) }
    val progress = animateFloatAsState(
        targetValue = if (checked.value) 1f else 0f,
        animationSpec = tween(durationMillis = duration, easing = LinearEasing)
    )

    LaunchedEffect(key1 = value) {
        if (value != checked.value) {
            checked.value = value
        }
    }

    Canvas(
        modifier = Modifier
            .size(size = size)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = {
                    checked.value = !checked.value
                    onChanged(checked.value)
                },
            )
    ) {
        val fillColor = lerp(
            start = Color.White,
            stop = Color(0xFFFF9900),
            fraction = progress.value / 0.5.coerceAtMost(1.0).toFloat()
        )

        drawRoundRect(
            style = Fill,
            color = fillColor,
            topLeft = Offset(0f, 0f),
            cornerRadius = CornerRadius(x = radius.toPx(), y = radius.toPx()),
        )
        drawRoundRect(
            style = Stroke(width = 1f),
            color = Color(0xFFFF9900),
            topLeft = Offset(0f, 0f),
            cornerRadius = CornerRadius(x = radius.toPx(), y = radius.toPx()),
        )

        if (progress.value >= 0.5) {
            val rect = Rect(Offset.Zero, this.size)
            val path = buildPath(
                rect,
                progress = ((progress.value - 0.5).coerceIn(0.0, 1.0) / 0.5).toFloat()
            )
            drawPath(
                path,
                color = Color.White,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

fun buildPath(rect: Rect, progress: Float): Path {
    val firstPoint = Offset(
        x = (rect.left + rect.width / 3.6).toFloat(),
        y = (rect.top + rect.height / 1.9).toFloat()
    )
    // "勾"的中间拐点位置
    val secondPoint = Offset(
        x = (rect.left + rect.width / 2.3).toFloat(),
        y = (rect.bottom - rect.height / 3.2).toFloat()
    )
    // "勾"的第三个点的位置
    val lastPoint = Offset(
        x = (rect.right - rect.width / 3.6).toFloat(),
        y = (rect.top + rect.height / 2.8).toFloat()
    )

    if (progress <= 0.5) {
        val fraction = (progress / 0.5).coerceIn(0.0, 1.0).toFloat()
        val offset = lerp(start = firstPoint, stop = secondPoint, fraction)

        return Path().apply {
            moveTo(firstPoint.x, firstPoint.y)
            lineTo(offset.x, offset.y)
        }
    } else {
        val fraction = ((progress - 0.5) / 0.5).coerceIn(0.0, 1.0).toFloat()
        val offset = lerp(start = secondPoint, stop = lastPoint, fraction)
        return Path().apply {
            moveTo(firstPoint.x, firstPoint.y)
            lineTo(secondPoint.x, secondPoint.y)
            lineTo(offset.x, offset.y)
        }
    }
}

