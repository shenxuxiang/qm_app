package com.example.qm_app.pages.map_location.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amap.api.maps.model.LatLng
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.pages.map_location.MapLocationViewModel
import com.example.qm_app.pages.map_location.UiState
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner8
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.white
import com.google.gson.Gson

@Composable
fun DisplayPoiListWidget(modifier: Modifier, viewModel: MapLocationViewModel, uiState: UiState) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(uiState.poiList) {
        lazyListState.animateScrollToItem(0, 0)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 60.dp, start = 12.dp, end = 12.dp)
                .fillMaxSize()
                .clip(corner8)
                .background(Color(0xDDFFFFFF))
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(text = "周边位置", fontSize = 14.sp, color = black4, lineHeight = 14.sp)
                    QmIcon(
                        QmIcons.Stroke.Refresh,
                        tint = MaterialTheme.colorScheme.primary,
                        size = 22.dp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = {
                                if (uiState.aMapView != null) {
                                    viewModel.updatePoiList(
                                        point = uiState.aMapView.aMap.cameraPosition.target,
                                        pageNum = uiState.pageNum + 1,
                                    )
                                }
                            })
                    )
                }

                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFD0D0D0))
                LazyColumn(state = lazyListState) {
                    itemsIndexed(uiState.poiList) { index, poi ->
                        DisplayPoiItemWidget(
                            data = poi,
                            isSelected = poi == uiState.selectedPoi,
                            bordered = index < uiState.poiList.size - 1,
                            onTap = { value ->
                                viewModel.updateUIState { it.copy(selectedPoi = value) }
                                uiState.aMapView?.moveCamera(
                                    LatLng(value.latLonPoint.latitude, value.latLonPoint.longitude),
                                    uiState.aMapView.aMap.cameraPosition.zoom,
                                )
                            }
                        )
                    }
                }
            }
            if (uiState.isLoading)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(white.copy(alpha = 0.8f))
                        .clickable(onClick = {}, indication = null, interactionSource = null),
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(30.dp),
                    )
                    Text(
                        "加载中···",
                        fontSize = 14.sp,
                        color = gray,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(44.dp)
                .clip(corner8)
                .background(white.copy(alpha = 0.8f))
                .clickable(onClick = {
                    val gson = Gson()
                    val json = gson.toJson(uiState.selectedPoi)
                    Router.controller.previousBackStackEntry?.savedStateHandle?.set(
                        "arguments",
                        json,
                    )
                    Router.popBackStack()
                }),
        ) {
            Text(
                text = "确认",
                fontSize = 16.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

