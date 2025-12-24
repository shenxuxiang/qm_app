package com.example.qm_app.pages.home.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.components.QmIcon
import com.example.qm_app.pages.home.HomeTabBar

@Composable
fun BottomNavBarItem(
    bottomTabBar: HomeTabBar,
    isSelected: Boolean,
    onTap: (value: String) -> Unit,
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f, animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing
        )
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .clickable(
                onClick = { onTap(bottomTabBar.route) },
                interactionSource = null,
                indication = null
            )
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                transformOrigin = TransformOrigin.Center
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QmIcon(
            icon = if (isSelected) bottomTabBar.selectedIcon else bottomTabBar.icon,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF4A4A4A),
            size = 22.dp,
        )
        Text(
            text = stringResource(bottomTabBar.resourceId),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF4A4A4A),
        )
    }
}

@Composable
fun BottomNavBar(tabKey: String, items: List<HomeTabBar>, onChanged: (value: String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp)
            .background(Color(0xFFF7F8F8))
            .drawBehind {
                drawLine(
                    color = Color(0xFFD9D9D9),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1f,
                    cap = StrokeCap.Square,
                )
            }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items.forEach { screen ->
            BottomNavBarItem(
                screen,
                isSelected = tabKey == screen.route,
                onTap = onChanged,
            )
        }
    }
}

