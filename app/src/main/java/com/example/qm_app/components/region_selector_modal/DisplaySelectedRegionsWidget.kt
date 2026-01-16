package com.example.qm_app.components.region_selector_modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.pages.main.RegionSourceTree
import com.example.qm_app.ui.theme.corner6
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white

/**
 * 展示选中的 Region
 * */
@Composable
fun DisplaySelectedRegionsWidget(
    selectedRegions: MutableList<RegionSourceTree>,
    onTap: (index: Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(selectedRegions.size) { index ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(32.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .background(color = primaryColor, shape = corner6)
                    .clickable(onClick = { onTap(index) })
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = selectedRegions[index].label,
                        fontSize = 14.sp,
                        color = white,
                    )
                    QmIcon(QmIcons.Stroke.Close, tint = white, size = 14.dp)
                }
            }
        }
    }
}