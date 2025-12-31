package com.example.qm_app.pages.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.corner18
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.primaryColor

@Composable
fun InputBox(
    value: String,
    enabled: Boolean = true,
    allowClear: Boolean = true,
    placeholder: String = "请输入",
    modifier: Modifier = Modifier,
    onChange: (input: String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    suffix: (@Composable () -> Unit) = {},
    prefix: (@Composable () -> Unit) = {},
) {
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    val visualTransformation =
        if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None

    BasicTextField(
        value = value,
        singleLine = true,
        enabled = enabled,
        onValueChange = { onChange(it) },
        visualTransformation = visualTransformation,
        cursorBrush = SolidColor(value = primaryColor),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.then(
            other = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clip(corner18)
                .background(color = primaryContainerColor)
        ),
        textStyle = TextStyle(
            color = black3,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            letterSpacing = if (keyboardType == KeyboardType.Password) 5.sp else 0.5.sp,
        ),
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            prefix()
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(),
            ) {
                if (value.isEmpty()) Text(
                    color = gray,
                    fontSize = 14.sp,
                    text = placeholder,
                    lineHeight = 14.sp,
                    letterSpacing = 0.5.sp,
                )
                innerTextField()
            }
            if (allowClear && value.isNotEmpty()) QmIcon(
                icon = QmIcons.Stroke.CloseCircle,
                tint = gray,
                size = 24.dp,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = { onChange("") }
                    )
            )
            suffix()
        }
    }
}