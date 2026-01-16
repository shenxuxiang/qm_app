package com.example.qm_app.pages.tab.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.api.NewsItem
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.corner6
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.white
import com.example.qm_app.utils.getNetworkAssetURL

@Composable
fun NewsItemBox(news: NewsItem) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            .height(76.dp)
            .background(color = white, shape = corner10)
            .padding(8.dp),
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(data = getNetworkAssetURL(news.primaryUrl))
                    .allowHardware(enable = false)
                    .build(),
                modifier = Modifier
                    .size(width = 96.dp, height = 60.dp)
                    .clip(shape = corner6),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = news.title,
                    maxLines = 2,
                    color = black3,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "发布时间：${news.updateTime}",
                    color = gray,
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                )
            }
        }
    }
}