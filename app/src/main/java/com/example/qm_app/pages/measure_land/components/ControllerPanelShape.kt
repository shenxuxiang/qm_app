package com.example.qm_app.pages.measure_land.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.qm_app.ui.theme.white

@Composable
fun ControllerPanelShape() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val rect = Rect(Offset(0f, 20.dp.toPx()), size);

        val radius = 20.dp.toPx();
        val path = Path().apply {
            moveTo(rect.left, rect.bottom)
            lineTo(rect.left, rect.top - radius)
            arcTo(
                Rect(
                    left = rect.left,
                    top = rect.top,
                    right = rect.left + radius * 2,
                    bottom = rect.top + radius * 2,
                ),

                startAngleDegrees = -180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )
            lineTo(rect.right - radius, rect.top)
            arcTo(
                Rect(
                    left = rect.right - 2 * radius,
                    top = rect.top,
                    right = rect.right,
                    bottom = rect.top + radius * 2,
                ),
                startAngleDegrees = -90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )
            lineTo(rect.right, rect.bottom)
            close();
        }
        drawPath(path = path, color = white)
        drawCircle(
            color = white,
            radius = 35.dp.toPx(),
            center = Offset(rect.width / 2, rect.top + 10.dp.toPx()),
        );


        val path2 = Path().apply {
            arcTo(
                Rect(
                    center = Offset(
                        rect.width / 2 - 46.1.dp.toPx(),
                        rect.top - 20.dp.toPx()
                    ),
                    radius = radius,
                ),
                startAngleDegrees = 33f,
                sweepAngleDegrees = 57f,
                forceMoveTo = false,
            )
            lineTo(rect.width / 2 - 46.1.dp.toPx(), rect.top)
            lineTo(rect.width / 2, rect.top)
            close();
        }

        val path3 = Path().apply {
            arcTo(
                Rect(
                    center = Offset(
                        rect.width / 2 + 46.1.dp.toPx(),
                        rect.top - 20.dp.toPx()
                    ),
                    radius = radius,
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 57f,
                forceMoveTo = false,
            )
            lineTo(rect.width / 2, rect.top)
            close()
        }
        drawPath(path = path2, color = white)
        drawPath(path = path3, color = white)
    }
}