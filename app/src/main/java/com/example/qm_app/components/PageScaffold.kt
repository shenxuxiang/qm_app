package com.example.qm_app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.R
import com.example.qm_app.common.QmApplication
import com.example.qm_app.common.QmIcons
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageScaffold(
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    val navController = QmApplication.navController
    val canPop = navController.previousBackStackEntry != null

    Box(modifier = Modifier.fillMaxSize()) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Image(
            painter = painterResource(R.drawable.background_img_variant),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            color = black3,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,
                        )
                    },
                    actions = actions,
                    expandedHeight = 56.dp,
                    navigationIcon = {
                        if (canPop) {
                            QmIcon(
                                icon = QmIcons.Stroke.Back,
                                size = 24.dp,
                                tint = black4,
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = null,
                                        onClick = { navController.popBackStack() },
                                    )
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                    ),
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}