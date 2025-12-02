package com.example.qm_app.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.pages.main.Screen

@Composable
fun BottomNavBarItem(screen: Screen, isSelected: Boolean, onTap: (value: String) -> Unit) {
    val transition = updateTransition(targetState = isSelected, label = "bottomNavBarTransition")
    val iconColor by transition.animateColor(transitionSpec = {
        tween(
            durationMillis = 300,
            easing = Ease
        )
    }) { tartState ->
        if (tartState) Color(0xFFFF9900) else Color(0xFF666666)
    }

    val scale by transition.animateFloat(transitionSpec = {
        tween(
            durationMillis = 300,
            easing = Ease
        )
    }) { tartState ->
        if (tartState) 1.1f else 1f
    }


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp)
            .clickable(
                onClick = { onTap(screen.route) },
                interactionSource = null,
                indication = null
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Icon(
            imageVector = screen.icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(26.dp)
                .scale(scale)
        )
        Text(
            text = stringResource(screen.resourceId),
            fontSize = 14.sp * scale,
            lineHeight = 14.sp * scale,
            color = iconColor,
        )
    }
}

@Composable
fun BottomNavBar(tabKey: String, items: List<Screen>, onChanged: (value: String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .drawBehind {
                drawLine(
                    color = Color(0xFFD9D9D9),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 0.5.dp.toPx(),
                    cap = StrokeCap.Square,
                )
            },
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

