package com.example.qm_app.pages.tab.mine.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.pages.tab.mine.MineMenuItem
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white

@Composable
fun MenuBoxWidget(title: String, menus: List<MineMenuItem>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(color = white, shape = corner10)
        )
    ) {
        Text(
            text = title,
            color = black3,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(vertical = 15.dp, horizontal = 12.dp)
        )
        FlowRow(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalArrangement = Arrangement.spacedBy(53.dp),
            modifier = Modifier
                .padding(top = 60.dp, bottom = 20.dp, start = 40.dp, end = 40.dp)
                .fillMaxSize()
        ) {
            repeat(menus.size) { index ->
                val menuItem = menus[index]

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(24.dp)
                        .wrapContentWidth(
                            align = Alignment.CenterHorizontally,
                            unbounded = true,
                        )
                        .clickable(onClick = { menuItem.link.invoke() }),
                ) {
                    Image(
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = menuItem.icon),
                    )
                    Text(
                        color = black4,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        text = menuItem.label,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
        }
    }
}