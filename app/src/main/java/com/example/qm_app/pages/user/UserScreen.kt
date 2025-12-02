package com.example.qm_app.pages.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(id: String) {
    val commonViewModel = QmApplication.commonViewModel
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "用户", fontSize = 18.sp, color = Color.White)
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF9900),
                    scrolledContainerColor = Color(0xFFFF9900)
                ),
            )
        }
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            Text("Hello World 你好北京 -- $id")
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                commonViewModel.navToMainScreen("home")
            }) {
                Text("To Home")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                commonViewModel.navToMainScreen("cart")
            }) {
                Text("To Cart")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                commonViewModel.navToMainScreen("favorite")
            }) {
                Text("To Favorite")
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                commonViewModel.navController.value!!.navigate("goods")
            }) {
                Text("To Goods")
            }
        }
    }
}
