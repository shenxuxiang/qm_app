package com.example.qm_app.pages.signup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.gray

@Composable
fun InputWidget(
    label: String,
    value: String,
    placeholder: String,
    allowClear: Boolean = true,
    modifier: Modifier = Modifier,
    onChange: (input: String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    suffix: @Composable () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
    ) {
        Text(text = label, fontSize = 14.sp, lineHeight = 14.sp, color = black4)
        Box(modifier = Modifier.weight(1f))
        BasicTextField(
            value = value,
            singleLine = true,
            onValueChange = onChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            cursorBrush = SolidColor(value = MaterialTheme.colorScheme.primary),
            visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            textStyle = TextStyle(
                color = black3,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                letterSpacing = if (keyboardType == KeyboardType.Password) 5.sp else 0.5.sp,
            ),
        ) { innerTextField ->
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) Text(
                        text = placeholder,
                        color = gray,
                        fontSize = 14.sp,
                        lineHeight = 14.sp
                    )
                    innerTextField()
                }
                suffix()
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
            }
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFCCCCCC))
    }
}