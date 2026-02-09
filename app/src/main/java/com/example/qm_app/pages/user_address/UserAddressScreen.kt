package com.example.qm_app.pages.user_address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.LoadMoreState
import com.example.qm_app.components.LoadMoreWidget
import com.example.qm_app.components.PageFootButton
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.components.QmCheckbox
import com.example.qm_app.components.QmIcon
import com.example.qm_app.components.rememberLoadMoreState
import com.example.qm_app.entity.UserAddressData
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.black6
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.white

@Composable
fun UserAddressScreen() {
    val lazyListState = rememberLazyListState()
    val loadMoreState = rememberLoadMoreState()
    val viewModel = hiltViewModel<UserAddressViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    fun onLoadMore() {
        val pageNum = uiState.pageNum
        val pageSize = uiState.pageSize
        viewModel.queryUserAddressList(pageNum + 1, pageSize)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.addressList }.collect {
            if (loadMoreState.state == LoadMoreState.LoadState.Loading) {
                loadMoreState.finishLoad(uiState.addressList.size >= uiState.total)
            }
        }
    }

    PageScaffold(
        title = "地址管理",
        bottomBar = {
            PageFootButton(text = "添加新地址") {
                Router.navigate(Route.AddUserAddressScreen.route)
            }
        }
    ) { paddingValues ->
        if (uiState.addressList.isNotEmpty())
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 12.dp)
            ) {
                itemsIndexed(uiState.addressList) { index, address ->
                    DisplayAddressItem(
                        address = address,
                        defaultAddress = uiState.defaultAddress,
                        onTap = {
                            if (address != uiState.defaultAddress) {
                                viewModel.setDefaultAddress(address)
                            }
                        })
                }
                item {
                    LoadMoreWidget(loadState = loadMoreState, onLoad = ::onLoadMore)
                }
            } else
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
            ) {
                QmIcon(icon = QmIcons.Stroke.Empty, tint = gray.copy(alpha = 0.6f), size = 46.dp)
                Text(
                    color = gray,
                    text = "暂无数据",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
    }
}

@Composable
fun DisplayAddressItem(
    address: UserAddressData,
    defaultAddress: UserAddressData?,
    onTap: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
            .height(300.dp)
            .background(color = white, shape = corner10)
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = "姓名：",
                color = black3,
                fontSize = 16.sp,
                lineHeight = 16.sp,
            )
            Text(
                text = address.username,
                color = black3,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = "联系电话：",
                color = black3,
                fontSize = 16.sp,
                lineHeight = 16.sp,
            )
            Text(
                text = address.phone,
                color = black3,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = "地址：",
                fontSize = 14.sp,
                color = black3,
                lineHeight = 22.sp,
            )
            Text(
                text = "${address.regionName}${address.address}",
                color = black3,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QmCheckbox(
                    value = defaultAddress == address,
                    size = 22.dp,
                    radius = 11.dp,
                    onChange = { onTap() },
                )
                Text(
                    text = "设置为默认",
                    color = black4,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                QmIcon(icon = QmIcons.Stroke.Edit, size = 22.dp, tint = black6)
                Text(
                    text = "编辑",
                    color = black4,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                )
            }
            Row(
                modifier = Modifier.padding(start = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QmIcon(icon = QmIcons.Stroke.Delete, size = 22.dp, tint = black6)
                Text(
                    text = "删除",
                    color = black4,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                )
            }
        }
    }
}
