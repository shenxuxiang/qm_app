package com.example.qm_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.modifier.BorderSide
import com.example.qm_app.modifier.border
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white

data class ButtonGroupOption(val value: String, val label: String)

@Composable
fun ButtonGroupWidget(
    buttonKey: String,
    modifier: Modifier,
    options: List<ButtonGroupOption>,
    onChange: (Int) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(options.size) { index ->
            val item = options[index]
            val isActivity = item.value == buttonKey
            val isFirstItem = index == 0
            val isLastItem = index == options.size - 1
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(
                        left = if (isFirstItem) null else BorderSide(
                            width = 1.dp,
                            color = primaryColor
                        ),
                        right = if (isLastItem) null else BorderSide(
                            width = 1.dp,
                            color = primaryColor
                        ),
                    )
                    .background(color = if (isActivity) primaryColor else white)
                    .clickable(onClick = { onChange(index) })
            ) {
                Text(
                    text = item.label,
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = if (isActivity) white else MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}