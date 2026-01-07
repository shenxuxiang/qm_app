package com.example.qm_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.RegionData
import com.example.qm_app.common.QmIcons
import com.example.qm_app.pages.login.components.InputBox
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.black6
import com.example.qm_app.ui.theme.corner6
import com.example.qm_app.ui.theme.white

@Composable
fun SelectorModalOfRegion(
    open: Boolean,
    value: String,
    onChange: (value: String) -> Unit,
    regionData: List<RegionData>,
) {
    var searchInput by rememberSaveable { mutableStateOf("") }

    BottomSheetWidget(
        open = open,
        onClose = { onChange("") },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(color = white)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                InputBox(
                    value = searchInput,
                    onChange = { searchInput = it },
                    placeholder = "请输入您要搜索的内容",
                    suffix = {
                        QmIcon(QmIcons.Stroke.Search, tint = black6, size = 22.dp)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
                ButtonWidget(
                    text = "确定",
                    shape = corner6,
                    type = ButtonWidgetType.Primary,
                    onTap = { },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(70.dp, 36.dp)
                )
            }
            Text(
                text = "请选择所在地区",
                fontSize = 16.sp,
                lineHeight = 16.sp,
                color = black4,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(100) { index ->
                    Text(text = "Hello world $index", modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }
    }
}