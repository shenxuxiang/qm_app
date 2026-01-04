package com.example.qm_app.pages.login

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.R
import com.example.qm_app.pages.login.components.AccountLogin
import com.example.qm_app.pages.login.components.FastLogin
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.corner2
import com.example.qm_app.ui.theme.white
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    val coroutineScope = rememberCoroutineScope()
    val primaryColor = MaterialTheme.colorScheme.primary
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val uiState by loginViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Image(
            painter = painterResource(R.drawable.background_img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 52.dp)
                    .size(94.dp)
                    .clickable(onClick = {
                        Router.navigate(Route.SignUpScreen.route)
                    }),
            )
            Column(
                modifier = Modifier
                    .padding(top = 33.dp, bottom = 28.dp, start = 12.dp, end = 12.dp)
                    .weight(weight = 1f)
                    .fillMaxWidth()
                    .clip(shape = corner10)
                    .background(color = white),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val pagerState =
                    rememberPagerState(initialPage = uiState.tabIndex, pageCount = { 2 })

                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .wrapContentSize(align = Alignment.Center)
                ) {
                    val tab0Color =
                        animateColorAsState(
                            targetValue = if (uiState.tabIndex == 0) primaryColor else black4,
                            animationSpec = tween(durationMillis = 300)
                        )
                    val tab1Color =
                        animateColorAsState(
                            targetValue = if (uiState.tabIndex == 1) primaryColor else black4,
                            animationSpec = tween(durationMillis = 300)
                        )
                    Text(
                        text = "快速登录",
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        color = tab0Color.value,
                        modifier = Modifier
                            .width(width = 66.dp)
                            .clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    loginViewModel.updateTabIndex(0)
                                    coroutineScope.launch { pagerState.scrollToPage(0) }
                                }
                            ),
                    )
                    Box(modifier = Modifier.size(width = 76.dp, height = 16.dp))
                    Text(
                        text = "账号登录",
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        color = tab1Color.value,
                        modifier = Modifier
                            .width(width = 66.dp)
                            .clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    loginViewModel.updateTabIndex(1)
                                    coroutineScope.launch { pagerState.scrollToPage(1) }
                                }
                            ),
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(width = 162.dp)
                ) {
                    val indicatorOffset =
                        animateDpAsState(
                            targetValue = if (uiState.tabIndex == 0) 0.dp else 142.dp,
                            animationSpec = tween(durationMillis = 300)
                        )
                    Box(
                        modifier = Modifier
                            .size(width = 20.dp, height = 3.dp)
                            .offset(x = indicatorOffset.value, 0.dp)
                            .clip(shape = corner2)
                            .background(color = primaryColor)
                    )
                }

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    beyondViewportPageCount = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) { tab ->
                    when (tab) {
                        0 -> FastLogin(loginViewModel)
                        1 -> AccountLogin()
                    }
                }
            }
        }
    }
}