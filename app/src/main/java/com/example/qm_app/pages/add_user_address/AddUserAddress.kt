package com.example.qm_app.pages.add_user_address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.components.FormItemInput
import com.example.qm_app.components.FormItemLocation
import com.example.qm_app.components.FormItemSelectRegion
import com.example.qm_app.components.PageFootButton
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.components.QmCheckbox
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white

@Composable
fun AddUserAddressScreen() {
    val scrollState = rememberScrollState()
    val viewModel = hiltViewModel<ViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    val disabled by derivedStateOf {
        uiState.userName.isEmpty() ||
                uiState.phone.isEmpty() ||
                uiState.regions.isEmpty() ||
                uiState.location.isEmpty()
    }

    PageScaffold(
        title = "添加地址",
        bottomBar = {
            PageFootButton(text = "立即保存", disabled = disabled) {
                val params = mapOf(
                    "phone" to uiState.phone,
                    "address" to uiState.location,
                    "username" to uiState.userName,
                    "defaultFlag" to uiState.isDefault,
                    "regionCode" to uiState.regions.last().value,
                    "regionName" to uiState.regions.last().label,
                )
                viewModel.add(params)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
                .verticalScroll(scrollState)
                .background(white, corner10)
                .padding(horizontal = 12.dp)
        ) {
            FormItemInput(
                label = "姓名",
                allowClear = false,
                value = uiState.userName,
                keyboardType = KeyboardType.Text,
            ) { input ->
                viewModel.updateUIState { it.copy(userName = input) }
            }
            FormItemInput(
                label = "联系电话",
                allowClear = false,
                value = uiState.phone,
                keyboardType = KeyboardType.Phone,
            ) { input ->
                viewModel.updateUIState { it.copy(phone = input) }
            }
            FormItemSelectRegion(
                label = "所在地区",
                value = uiState.regions,
            ) { input ->
                viewModel.updateUIState { it.copy(regions = input) }
            }
            FormItemLocation(label = "详细地址", value = uiState.location) { input ->
                viewModel.updateUIState { it.copy(location = input) }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(62.dp)
                    .fillMaxWidth(),
            ) {
                QmCheckbox(
                    value = uiState.isDefault,
                    size = 22.dp,
                    radius = 11.dp,
                    onChange = { checked ->
                        viewModel.updateUIState { it.copy(isDefault = checked) }
                    }
                )
                Text(
                    text = "设置为默认地址",
                    color = black4,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}

