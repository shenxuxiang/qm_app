package com.example.qm_app.modifier

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * x、y 表示阴影的偏移量
 * blur 表示阴影的模糊半径
 * spread 表示阴影的扩展半径
 * corner 表示阴影的圆角半角
 * color 表示阴影的颜色
 */
fun Modifier.boxShadow(
    x: Dp = 2.dp,
    y: Dp = 2.dp,
    blur: Dp = 4.dp,
    spread: Dp = 0.dp,
    corner: Dp = 0.dp,
    color: Color = Color(0xA0000000),
): Modifier = composed {
    val density = LocalDensity.current
    val blurPx = remember { with(density) { blur.toPx() } }
    val spreadPx = remember { with(density) { spread.toPx() } }
    val cornerPx = remember { with(density) { corner.toPx() } }
    val offset = remember { with(density) { Offset(x = x.toPx(), y = y.toPx()) } }

    this.drawBehind {
        // 创建画笔，并设置画笔的颜色
        val paint = Paint().apply { this.color = color }
        // 获取 Android 原生 Framework 中的 Paint 对象
        val frameworkPaint = paint.asFrameworkPaint()
        // 设置模糊半径、模糊样式
        frameworkPaint.maskFilter = BlurMaskFilter(
            blurPx,
            BlurMaskFilter.Blur.NORMAL
        )

        // 根据 spreadRadius 调整绘制区域
        val top = -spreadPx + offset.y
        val left = -spreadPx + offset.x
        val right = size.width + spreadPx + offset.x
        val bottom = size.height + spreadPx + offset.y

        // drawIntoCanvas 允许开发者直接进入原生的 canvas 绘制上下文
        drawIntoCanvas { canvas ->
            canvas.drawRoundRect(
                top = top,
                left = left,
                paint = paint,
                right = right,
                bottom = bottom,
                radiusX = cornerPx,
                radiusY = cornerPx,
            )
        }
    }
}