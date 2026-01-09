package com.example.qm_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.RegionSourceTree
import com.example.qm_app.common.QmIcons
import com.example.qm_app.pages.login.components.InputBox
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.black6
import com.example.qm_app.ui.theme.corner6
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white
import com.github.promeg.pinyinhelper.Pinyin
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties

private data class DisplayRegionGroup(
    val firstCharacter: String,
    val regions: MutableList<RegionSourceTree>,
)

data class SelectedOptionItem(val label: String, val value: String)

private fun regionDataFormat(regionData: List<RegionSourceTree>): MutableList<DisplayRegionGroup> {
    val regionList = mutableListOf<DisplayRegionGroup>()

    for (region in regionData) {
        val firstCharacter: String = Pinyin.toPinyin(region.label, "").take(1)
        val filterResult = regionList.find { it.firstCharacter == firstCharacter }
        if (filterResult == null) {
            regionList.add(
                DisplayRegionGroup(
                    firstCharacter,
                    regions = mutableListOf(region)
                )
            )
        } else {
            filterResult.regions.add(region)
        }
    }

    regionList.sortWith(Comparator { a, b -> a.firstCharacter.compareTo(b.firstCharacter) })
    return regionList
}


@Composable
fun SelectorModalOfRegion(
    open: Boolean,
    value: List<SelectedOptionItem>,
    onChange: (value: String) -> Unit,
    regionData: List<RegionSourceTree>,
) {
    var searchInput by remember { mutableStateOf("") }
    val selectedRegion = remember { mutableStateListOf<RegionSourceTree>() }
    val displayRegionGroups = remember { mutableStateOf<List<DisplayRegionGroup>>(emptyList()) }

    LaunchedEffect(open, regionData) {
        if (open && regionData.isNotEmpty() && displayRegionGroups.value.isEmpty()) {
            displayRegionGroups.value = regionDataFormat(regionData)
        }
    }

    LaunchedEffect(open, value) {
        var regionDataList = regionData

        for (item in value) {
            val filterResult = regionDataList.find { item.value == it.value }
            if (filterResult == null) break
            selectedRegion.add(filterResult)
            if (filterResult.children != null) regionDataList = filterResult.children
        }
    }

    if (open)
        BottomSheetDialog(
            onDismissRequest = { onChange("") },
            properties = BottomSheetDialogProperties()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(color = white)
            ) {
                if (regionData.isEmpty())
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = primaryColor,
                            strokeWidth = 2.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                Column(modifier = Modifier.fillMaxSize()) {
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
                            onTap = {},
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .size(70.dp, 36.dp)
                        )
                    }
                    if (selectedRegion.isEmpty()) {
                        Text(
                            text = "请选择所在地区",
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            color = black4,
                            modifier = Modifier.padding(
                                top = 6.dp,
                                end = 12.dp,
                                start = 12.dp,
                                bottom = 12.dp,
                            )
                        )
                    } else {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp, start = 12.dp, end = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            repeat(selectedRegion.size) { index ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .height(32.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .background(color = primaryColor, shape = corner6)
                                        .clickable(onClick = {
                                            selectedRegion.removeRange(index, selectedRegion.size)
                                            if (index == 0) {
                                                displayRegionGroups.value =
                                                    regionDataFormat(regionData)
                                            } else {
                                                displayRegionGroups.value =
                                                    regionDataFormat(selectedRegion[index - 1].children!!)
                                            }
                                        })
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = selectedRegion[index].label,
                                            fontSize = 14.sp,
                                            color = white,
                                        )
                                        QmIcon(QmIcons.Stroke.Close, tint = white, size = 14.dp)
                                    }
                                }
                            }
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        displayRegionGroups.value.forEach { (firstCharacter, regions) ->
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
                                            onClick = {
                                                if (region.children?.isNotEmpty() ?: false) {
                                                    displayRegionGroups.value =
                                                        regionDataFormat(region.children)
                                                }

                                                val size = selectedRegion.size
                                                val lastSelectedRegion =
                                                    if (selectedRegion.isNotEmpty()) selectedRegion[size - 1]
                                                    else null

                                                if (selectedRegion.isNotEmpty() && lastSelectedRegion?.children == null) selectedRegion.removeAt(
                                                    index = size - 1
                                                )

                                                selectedRegion.add(region)
                                            },
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
            }
        }
}