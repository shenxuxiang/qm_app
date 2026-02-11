package com.example.qm_app.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp

data class BorderSide(val color: Color, val width: Dp)

fun Modifier.border(horizontal: BorderSide? = null, vertical: BorderSide? = null): Modifier =
    composed(
        inspectorInfo = debugInspectorInfo {
            name = "border"
            properties["vertical"] = vertical
            properties["horizontal"] = horizontal
        }
    ) {
        this.drawBehind {
            val rect = Rect(Offset.Zero, this.size)

            if (horizontal != null) {
                val width = horizontal.width
                val color = horizontal.color
                val pathLeft = Path().apply {
                    moveTo(rect.left, rect.top)
                    lineTo(rect.left, rect.bottom)
                }
                val pathRight = Path().apply {
                    moveTo(rect.right, rect.top)
                    lineTo(rect.right, rect.bottom)
                }

                drawPath(path = pathLeft, color = color, style = Stroke(width = width.toPx()))
                drawPath(path = pathRight, color = color, style = Stroke(width = width.toPx()))
            }
            if (vertical != null) {
                val width = vertical.width
                val color = vertical.color
                val pathTop = Path().apply {
                    moveTo(rect.left, rect.top)
                    lineTo(rect.right, rect.top)
                }
                val pathBottom = Path().apply {
                    moveTo(rect.left, rect.bottom)
                    lineTo(rect.right, rect.bottom)
                }

                drawPath(path = pathTop, color = color, style = Stroke(width = width.toPx()))
                drawPath(path = pathBottom, color = color, style = Stroke(width = width.toPx()))
            }
        }
    }

fun Modifier.border(
    left: BorderSide? = null,
    top: BorderSide? = null,
    right: BorderSide? = null,
    bottom: BorderSide? = null,
) =
    composed(
        inspectorInfo = debugInspectorInfo {
            name = "border"
            properties["left"] = left
            properties["top"] = top
            properties["right"] = right
            properties["bottom"] = bottom
        }
    ) {
        this.drawBehind {
            val rect = Rect(Offset.Zero, this.size)

            if (left != null) {
                val path = Path().apply {
                    moveTo(rect.left, rect.top)
                    lineTo(rect.left, rect.bottom)
                }
                drawPath(path = path, color = left.color, style = Stroke(width = left.width.toPx()))
            }
            if (top != null) {
                val path = Path().apply {
                    moveTo(rect.left, rect.top)
                    lineTo(rect.right, rect.top)
                }
                drawPath(path = path, color = top.color, style = Stroke(width = top.width.toPx()))
            }
            if (right != null) {
                val path = Path().apply {
                    moveTo(rect.right, rect.top)
                    lineTo(rect.right, rect.bottom)
                }
                drawPath(
                    path = path,
                    color = right.color,
                    style = Stroke(width = right.width.toPx())
                )
            }
            if (bottom != null) {
                val path = Path().apply {
                    moveTo(rect.left, rect.bottom)
                    lineTo(rect.right, rect.bottom)
                }
                drawPath(
                    path = path,
                    color = bottom.color,
                    style = Stroke(width = bottom.width.toPx())
                )
            }
        }
    }