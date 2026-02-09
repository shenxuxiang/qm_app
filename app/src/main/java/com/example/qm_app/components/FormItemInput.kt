package com.example.qm_app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.gray


@Composable
fun FormItemInput(
    label: String,
    value: String,
    bordered: Boolean = true,
    allowClear: Boolean = true,
    suffix: (@Composable () -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onChange: (input: String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp)
            .drawBehind {
                if (bordered)
                    drawLine(
                        strokeWidth = 1.dp.toPx(),
                        color = Color(0xFFE0E0E0),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height)
                    )
            },
    ) {
        Text(text = label, fontSize = 14.sp, color = black4, lineHeight = 14.sp)
        BasicTextField(
            value = value,
            singleLine = true,
            keyboardActions = keyboardActions,
            onValueChange = { onChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxHeight()
                .weight(1f),
            textStyle = TextStyle(
                color = black4,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.End,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty())
                        Text(
                            text = "请输入${label}",
                            color = gray,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        innerTextField()
                    }
                }
            }
        )
        if (value.isNotEmpty() && allowClear)
            QmIcon(
                icon = QmIcons.Stroke.CloseCircle,
                tint = gray,
                size = 22.dp,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = { onChange("") })
            )
        if (suffix != null) suffix()
    }
}