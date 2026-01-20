package com.example.qm_app.pages.user_auth_farm_view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white
import com.example.qm_app.utils.getNetworkAssetURL

@Composable
fun PhotoBoxWidget(title: String, url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(corner10)
                .background(color = white)
        )
    ) {
        Text(
            text = title,
            color = black3,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp, start = 12.dp)
        )
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(getNetworkAssetURL(url))
                .allowHardware(false)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(
                    top = 48.dp,
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 16.dp
                )
                .fillMaxSize()
        )
    }
}