package com.example.qm_app.components.region_selector_modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.pages.main.RegionSourceTree
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.black3

/**
 * 展示选项列表
 * */
@Composable
fun DisplayRegionGroupsWidget(
    modifier: Modifier,
    lazyListState: LazyListState,
    displayRegionGroups: List<DisplayRegionGroup>,
    onTap: (region: RegionSourceTree) -> Unit,
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier.then(Modifier.fillMaxWidth()),
    ) {
        displayRegionGroups.forEach { (firstCharacter, regions) ->
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .background(color = Color(0xFFE9E9E9))
                        .padding(start = 10.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = firstCharacter,
                        color = black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            itemsIndexed(regions) { _, region ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable(
                            onClick = { onTap(region) },
                        )
                ) {
                    Text(
                        text = region.label,
                        color = black3,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
        }
    }
}