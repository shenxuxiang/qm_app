package com.example.qm_app.components.region_selector_modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.modifier.boxShadow
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.corner12
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white

/**
 * 地区分组（首字母）
 * */
@Composable
fun DisplayFirstCharIndexBar(
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