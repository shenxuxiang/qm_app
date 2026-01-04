package com.example.qm_app.pages.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.qm_app.R
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router

@Composable
fun SignUpScreen() {
    PageScaffold(title = "注册") { paddingValues ->
        Image(
            painter = painterResource(R.drawable.image1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .clickable(onClick = {
                    Router.navigate(Route.GoodsScreen.route)
                })
        )
    }
}