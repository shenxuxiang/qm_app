package com.example.qm_app.pages.tab.mine.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.R
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.QmIcon
import com.example.qm_app.compositionLocal.LocalUserInfo
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.white
import com.example.qm_app.utils.getNetworkAssetURL

@Composable
fun HeaderWidget() {
    val userInfo = LocalUserInfo.current

    val avatarImage = if (userInfo?.avatar?.isNotEmpty() ?: false)
        getNetworkAssetURL(userInfo.avatar) else
        R.drawable.default_avatar

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(top = 49.dp, start = 20.dp)
    ) {
        AsyncImage(
            contentDescription = null,
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = avatarImage)
                .allowHardware(false)
                .crossfade(enable = true)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 4.dp)
                .clip(CircleShape)
                .size(54.dp)
                .drawWithContent {
                    drawContent()
                    drawCircle(
                        color = white,
                        radius = 27.dp.toPx(),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = userInfo?.username ?: "",
                fontSize = 16.sp,
                color = black3,
                lineHeight = 16.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QmIcon(icon = QmIcons.Stroke.Phone, size = 16.dp, tint = black3)
                Text(
                    text = userInfo?.phone ?: "",
                    color = black4,
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QmIcon(icon = QmIcons.Stroke.Location, size = 16.dp, tint = black3)
                Text(
                    text = userInfo?.regionName ?: "",
                    maxLines = 1,
                    color = black4,
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }

}