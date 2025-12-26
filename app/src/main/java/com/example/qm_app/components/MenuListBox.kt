package com.example.qm_app.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white

data class MenuItemOption(val label: String, @param:DrawableRes val res: Int)

@Composable
private fun MenuItem(label: String, res: Int) {
    ConstraintLayout(
        modifier = Modifier
            .size(32.dp, 55.dp)
            .wrapContentWidth(// unbounded-true 子元素将不受父容器边界的影响
                Alignment.CenterHorizontally,
                unbounded = true
            )
    ) {
        val (imageRef, textRef) = createRefs()
        val guideLine = createGuidelineFromStart(0.5f)
        createVerticalChain(imageRef, textRef, chainStyle = ChainStyle.SpreadInside)
        Image(
            painter = painterResource(res),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp)
                .constrainAs(imageRef) {
                    centerAround(guideLine) // 水平居中
                }
        )
        Text(
            text = label,
            color = black4,
            fontSize = 13.sp,
            lineHeight = 13.sp,
            modifier = Modifier
                .constrainAs(textRef) {
                    centerAround(guideLine) // 水平居中
                }
        )
    }
}


@Composable
fun MenuListBox(options: List<MenuItemOption>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .background(color = white, shape = corner10)
            .padding(vertical = 20.dp, horizontal = 29.dp)
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(50.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            options.forEach { item ->
                MenuItem(label = item.label, res = item.res)
            }
        }
    }
}