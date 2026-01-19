package com.example.qm_app.pages.user_auth_farm.components

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white

@Composable
fun PhotoCardWidget(
    title: String,
    photo: Bitmap?,
    subtitle: String,
    @DrawableRes resourceId: Int,
    modifier: Modifier = Modifier,
    onTap: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(corner10)
                .background(color = white)
        )
    ) {
        if (photo == null) {
            Box(modifier = modifier.then(Modifier.fillMaxSize())) {
                Image(
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    painter = painterResource(resourceId),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(209.dp, 130.dp),
                )
                Text(
                    title,
                    color = black3,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp, start = 12.dp),
                )
                Text(
                    subtitle,
                    color = black4,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 44.dp, start = 12.dp),
                )
                QmIcon(
                    icon = QmIcons.Filled.TabBarMenuRelease,
                    tint = MaterialTheme.colorScheme.primary,
                    size = 46.dp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 36.dp, end = 81.dp)
                        .clickable(
                            indication = null,
                            onClick = { onTap() },
                            interactionSource = null
                        )
                )
            }
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photo)
                    .allowHardware(false)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.clickable(
                    indication = null,
                    onClick = { onTap() },
                    interactionSource = null
                )
            )
        }
    }
}