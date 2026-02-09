package com.example.qm_app.pages.signup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmApplication
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.components.region_selector_modal.RegionSelectorModal
import com.example.qm_app.entity.SelectedOptionItem
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionWidget(
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    value: List<SelectedOptionItem>,
    onChange: (List<SelectedOptionItem>) -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }
    val mainViewModel = QmApplication.mainViewModel
    val uiState by mainViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = {
                            focusManager.clearFocus()
                            showDialog.value = true
                            if (uiState.regionData.isEmpty()) mainViewModel.queryRegionData()
                        },
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (value.isEmpty()) placeholder else value.joinToString { it.label },
                    color = if (value.isEmpty()) gray else black3,
                    maxLines = 1,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            QmIcon(QmIcons.Stroke.Forward, tint = gray, size = 24.dp)
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFCCCCCC))
        RegionSelectorModal(
            value = value,
            open = showDialog.value,
            regionData = uiState.regionData,
            onConfirm = {
                onChange(it)
                showDialog.value = false
            },
            onCancel = { showDialog.value = false }
        )
    }
}