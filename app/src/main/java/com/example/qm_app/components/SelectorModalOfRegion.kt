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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.entity.SelectedOptionItem
import com.example.qm_app.modifier.boxShadow
import com.example.qm_app.pages.login.components.InputBox
import com.example.qm_app.pages.main.RegionSourceTree
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.black6
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.corner12
import com.example.qm_app.ui.theme.corner6
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white
import com.github.promeg.pinyinhelper.Pinyin
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import kotlinx.coroutines.launch

private data class DisplayRegionGroup(
    val firstCharacter: String,
    val regions: MutableList<RegionSourceTree>,
)

/**
 * 地区格式化函数，将地区名称转成拼音，再根据拼音首字母进行排序；
 * 将首字母相同的 region 放在同一个数组中；
 * */
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
    onCancel: () -> Unit,
    value: List<SelectedOptionItem>,
    regionData: List<RegionSourceTree>,
    onConfirm: (value: List<SelectedOptionItem>) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    // 搜索内容
    var searchInput by remember { mutableStateOf("") }
    // 搜索结果
    val searchResults = remember { mutableStateOf(emptyList<RegionSourceTree>()) }
    // 用户当前选中的项目列表
    val selectedRegions = remember { mutableStateListOf<RegionSourceTree>() }
    // 当前展示的列表选项
    val displayRegionGroups = remember { mutableStateOf<List<DisplayRegionGroup>>(emptyList()) }

    // 当前展示的列表的（首字母）选项
    val displayRegionFirstCharacterGroups =
        derivedStateOf { displayRegionGroups.value.map { it.firstCharacter } }

    // 记录所有 stickyHeader 在 LazyListState 中对应的 firstVisibleItemIndex
    val stickyHeaderIndexGroups = derivedStateOf {
        var i = 0
        val list = mutableListOf<Int>()
        for (group in displayRegionGroups.value) {
            list.add(i)
            i += (group.regions.size + 1)
        }
        list
    }

    // 当前吸附在容器顶部的 stickyHeader 是第几个，默认 0
    val currentStickyHeaderIndex = remember { mutableStateOf(0) }

    /**
     * 如果 lazyListState.firstVisibleItemIndex 不大于某个 index，
     * 说明 LazyColumn 还没有滚动到这个 index 位置，
     * 那么此时，currentStickyHeaderIndex 取当前最大的那个就可以
     * */
    LaunchedEffect(displayRegionGroups.value) {
        lazyListState.scrollToItem(0)

        snapshotFlow { lazyListState.firstVisibleItemIndex }.collect {
            /**
             * for 循环至少会执行一次；
             * 因为 it >= 0，stickyHeaderIndexGroups >= 0；
             * 所以，索引 i 默认值应该设置为 -1。
             * */
            var i = -1
            for (index in stickyHeaderIndexGroups.value) {
                if (it >= index) {
                    i += 1
                } else {
                    break
                }
            }
            currentStickyHeaderIndex.value = i
        }
    }

    LaunchedEffect(open, value, regionData) {
        if (open && regionData.isNotEmpty()) {
            /**
             * 如果 value 是空的，说明此时用户还没有选中任何地区，那么 displayRegionGroups 应该是所有的省份，同时也不需要计算 selectedRegion
             * 如果 value 不是空的，那么就应该根据具体的情况再进行分析
             * */
            if (value.isEmpty()) {
                selectedRegions.clear()
                displayRegionGroups.value = regionDataFormat(regionData)
            } else {
                /**
                 * 如果 selectedRegions 为空，而 value 是不为空的，所以肯定要重新结算 selectedRegion、displayRegionGroups；
                 * 如果 selectedRegions 不为空，根据计算得出最新的 value，并赋值给 currentValue 变量；
                 * 如果 value == currentValue，则不需要重新计算 selectedRegion、displayRegionGroups，否则重新计算；
                 * */
                var hasMatched = false
                if (selectedRegions.isNotEmpty()) {
                    val currentValue = selectedRegions.map {
                        SelectedOptionItem(label = it.label, value = it.value)
                    }
                    hasMatched = currentValue == value
                }
                if (!hasMatched) {
                    var regionDataList = regionData
                    // 重新计算之前，清空
                    selectedRegions.clear()
                    for (item in value) {
                        val filterResult = regionDataList.find { item.value == it.value }
                        if (filterResult == null) break
                        selectedRegions.add(filterResult)
                        if (filterResult.children != null) regionDataList = filterResult.children
                    }

                    val size = selectedRegions.size
                    if (size > 1 && selectedRegions[size - 2].children != null) {
                        displayRegionGroups.value =
                            regionDataFormat(selectedRegions[size - 2].children!!)
                    } else {
                        displayRegionGroups.value = regionDataFormat(regionData)
                    }
                }
            }
        }
    }

    fun handleSelectRegion(region: RegionSourceTree) {
        if (region.children?.isNotEmpty() ?: false) {
            displayRegionGroups.value = regionDataFormat(region.children)
        }

        val size = selectedRegions.size
        val lastSelectedRegion =
            if (selectedRegions.isNotEmpty()) selectedRegions[size - 1]
            else null

        // 如果选中列表中最后一个 region 已经没有子项了，那么当用户再次选中一个 region 时，
        // 应该替换而不是新增。
        if (selectedRegions.isNotEmpty() && lastSelectedRegion?.children == null) selectedRegions.removeAt(
            index = size - 1
        )

        selectedRegions.add(region)
    }

    if (open)
        BottomSheetDialog(
            onDismissRequest = {
                onCancel()
                searchInput = ""
                // 下面这两行代码纯粹是为了在打开用户再次打开弹框时，避免出现页面抖动。
                selectedRegions.clear()
                displayRegionGroups.value = emptyList()
            },
            properties = BottomSheetDialogProperties()
        ) {
            // 注意，这个 anchorView 将作为 Toast 组件插入进来的插槽，而且还必须在 BottomSheetDialog 内部，
            // 否则会被 BottomSheetDialog 背景层覆盖
            val anchorView = LocalView.current

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .drawBehind { // 绘制一个左上（角）、右上（角）带圆角的白色背景
                        val path = Path().apply {
                            val radius = 10.dp.toPx()
                            moveTo(size.width, size.height)
                            lineTo(0f, size.height)
                            lineTo(0f, radius)
                            arcTo(
                                Rect(
                                    center = Offset(x = radius, y = radius),
                                    radius = radius
                                ),
                                startAngleDegrees = 180f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false,
                            )
                            lineTo(x = size.width - radius, y = 0f)
                            arcTo(
                                Rect(
                                    center = Offset(x = size.width - radius, y = radius),
                                    radius = radius
                                ),
                                startAngleDegrees = -90f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false,
                            )
                            close()
                        }

                        drawPath(path = path, color = white, style = Fill)
                    }
            ) {
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
                            onChange = {
                                searchInput = it
                                val regions = mutableListOf<RegionSourceTree>()
                                if (it.isNotEmpty()) {
                                    for (group in displayRegionGroups.value) {
                                        for (region in group.regions) {
                                            if (region.label.contains(it)) {
                                                regions.add(region)
                                            }
                                        }
                                    }
                                }
                                searchResults.value = regions
                            },
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
                            onTap = {
                                if (selectedRegions.isEmpty()) {
                                    Toast.postShowWarningToast(
                                        view = anchorView,
                                        message = "请选择所在地区",
                                        alignment = Alignment.Center,
                                    )
                                } else {
                                    onConfirm(
                                        selectedRegions.map {
                                            SelectedOptionItem(
                                                label = it.label,
                                                value = it.value,
                                            )
                                        }
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .size(70.dp, 36.dp)
                        )
                    }
                    if (searchInput.isEmpty()) {
                        if (selectedRegions.isEmpty()) {
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
                            /* 展示被选中的地区 */
                            DisplaySelectedRegionsWidget(selectedRegions) { index ->
                                selectedRegions.removeRange(
                                    index,
                                    selectedRegions.size
                                )
                                if (index == 0) {
                                    displayRegionGroups.value =
                                        regionDataFormat(regionData)
                                } else {
                                    displayRegionGroups.value =
                                        regionDataFormat(selectedRegions[index - 1].children!!)
                                }
                            }
                        }

                        /* 展示地区选项列表 */
                        DisplayRegionGroupsWidget(
                            lazyListState = lazyListState,
                            modifier = Modifier.weight(1f),
                            onTap = { handleSelectRegion(it) },
                            displayRegionGroups = displayRegionGroups.value,
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            if (searchResults.value.isEmpty())
                                items(1) {
                                    Text(
                                        text = "没有搜索到结果~",
                                        color = gray,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp),
                                    )
                                }
                            itemsIndexed(searchResults.value) { _, region ->
                                Box(
                                    contentAlignment = Alignment.CenterStart,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .clickable(onClick = {
                                            searchInput = ""
                                            handleSelectRegion(region)
                                        })
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

                if (searchInput.isEmpty()) {
                    /* 首字母索引栏 */
                    FirstCharIndexBar(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        characters = displayRegionFirstCharacterGroups.value,
                        currentStickyHeaderIndex = currentStickyHeaderIndex.value,
                    ) { index ->
                        coroutineScope.launch {
                            var i = 0
                            for (item in displayRegionGroups.value) {
                                if (item.firstCharacter == displayRegionFirstCharacterGroups.value[index]) break
                                // 计算时应该算上 stickyHeader，所以需要 +1
                                i += (item.regions.size + 1)
                            }

                            lazyListState.scrollToItem(i)
                            currentStickyHeaderIndex.value = index
                        }
                    }
                }
                /* 展示加载 Loading */
                LoadingWidget(isShow = regionData.isEmpty())
            }
        }
}

/**
 * 展示选中的 Region
 * */
@Composable
private fun DisplaySelectedRegionsWidget(
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

/**
 * 展示选项列表
 * */
@Composable
private fun DisplayRegionGroupsWidget(
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

/**
 * 地区分组（首字母）
 * */
@Composable
private fun FirstCharIndexBar(
    modifier: Modifier,
    characters: List<String>,
    currentStickyHeaderIndex: Int,
    onTap: (index: Int) -> Unit,
) {
    Column(
        modifier = modifier.then(
            Modifier
                .padding(end = 12.dp)
                .boxShadow(corner = 12.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .background(color = white, shape = corner12)
                .padding(vertical = 16.dp)
        ),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        repeat(characters.size) { idx ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (currentStickyHeaderIndex == idx) primaryColor else white
                    )
                    .clickable(onClick = { onTap(idx) }),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = characters[idx],
                    color = if (currentStickyHeaderIndex == idx) white else black3,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                )
            }
        }
    }
}

/**
 * 加载中。。。
 * */
@Composable
private fun LoadingWidget(isShow: Boolean) {
    if (isShow)
        Box(
            contentAlignment = BiasAlignment(0f, -0.2f),
            modifier = Modifier
                .fillMaxSize()
                .background(color = white.copy(0.6f), shape = corner10)
                .clickable(indication = null, interactionSource = null, onClick = {}),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = primaryColor,
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "数据加载中···",
                    fontSize = 14.sp,
                    color = gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
}