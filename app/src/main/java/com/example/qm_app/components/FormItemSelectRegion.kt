package com.example.qm_app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmApplication
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.region_selector_modal.RegionSelectorModal
import com.example.qm_app.entity.SelectedOptionItem
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.gray

@Composable
fun FormItemSelectRegion(
    label: String,
    bordered: Boolean = true,
    value: List<SelectedOptionItem>,
    onChange: (input: List<SelectedOptionItem>) -> Unit,
) {
    val showModel = remember { mutableStateOf(false) }
    val mainViewModel = QmApplication.mainViewModel
    val mainUIState by mainViewModel.uiState.collectAsState()
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = {
                    showModel.value = true
                    if (mainUIState.regionData.isEmpty()) mainViewModel.queryRegionData()
                },
            )
            .drawBehind {
                if (bordered)
                    drawLine(
                        strokeWidth = 0.5.dp.toPx(),
                        color = Color(0xFFE0E0E0),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height)
                    )
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = label, fontSize = 14.sp, color = black4, lineHeight = 14.sp)
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
            ) {
                if (value.isNotEmpty()) {
                    Text(
                        text = value.joinToString { it.label },
                        maxLines = 1,
                        color = black4,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    Text(
                        text = "请输入${label}",
                        color = gray,
                        fontSize = 14.sp,
                        lineHeight = 14.sp
                    )
                }
                QmIcon(icon = QmIcons.Stroke.Forward, tint = gray, size = 22.dp)
            }
        }
        RegionSelectorModal(
            value = value,
            open = showModel.value,
            regionData = mainUIState.regionData,
            onConfirm = {
                onChange(it)
                showModel.value = false
            },
            onCancel = { showModel.value = false }
        )
    }
}