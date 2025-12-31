package com.example.qm_app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.black6
import com.example.qm_app.ui.theme.corner18
import com.example.qm_app.ui.theme.errorColor
import com.example.qm_app.ui.theme.onErrorColor
import com.example.qm_app.ui.theme.onPrimaryColor
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white

sealed class ButtonWidgetType(val bgColor: Color, val color: Color, val borderColor: Color) {
    object Primary :
        ButtonWidgetType(bgColor = primaryColor, color = onPrimaryColor, borderColor = primaryColor)

    object Danger :
        ButtonWidgetType(bgColor = errorColor, color = onErrorColor, borderColor = errorColor)

    object Default : ButtonWidgetType(bgColor = white, color = black4, borderColor = black6)
}

@Composable
fun ButtonWidget(
    text: String,
    ghost: Boolean = false,
    shape: Shape = corner18,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    type: ButtonWidgetType = ButtonWidgetType.Default,
    onTap: () -> Unit,
) {
    val textColor = if (type is ButtonWidgetType.Default) {
        type.color
    } else {
        if (ghost) {
            type.bgColor
        } else {
            type.color
        }
    }

    val bgColor = if (type is ButtonWidgetType.Default) {
        type.bgColor
    } else {
        if (ghost) {
            white
        } else {
            type.bgColor
        }
    }

    OutlinedButton(
        shape = shape,
        enabled = !disabled,
        onClick = { onTap() },
        border = BorderStroke(width = 1.dp, type.borderColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            disabledContainerColor = bgColor,
        ),
        modifier = modifier.then(
            other = Modifier
                .padding(all = 0.dp)
                .drawWithContent {
                    drawContent()
                    if (disabled) drawRect(
                        color = white.copy(0.4f),
                        topLeft = Offset(x = 0f, y = 0f),
                    )
                }
        ),
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = textColor,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}