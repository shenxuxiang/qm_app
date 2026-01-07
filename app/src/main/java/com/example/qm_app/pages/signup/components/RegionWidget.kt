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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmApplication
import com.example.qm_app.components.SelectorModalOfRegion
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.gray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionWidget(modifier: Modifier = Modifier, label: String, placeholder: String) {
    val showDialog = remember { mutableStateOf(false) }
    val commonViewModel = QmApplication.commonViewModel
    val uiState by commonViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
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
                    .clickable(onClick = {
                        coroutineScope.launch {
                            Toast.showSuccessToast("数据记载中，请稍后")
                        }
                    }),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = placeholder,
                    color = gray,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
            }
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFCCCCCC))

        SelectorModalOfRegion(
            value = "",
            open = showDialog.value,
            regionData = uiState.regionData,
            onChange = { showDialog.value = false },
        )
    }
}