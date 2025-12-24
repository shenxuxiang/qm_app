package com.example.qm_app.pages.user

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.R
import com.example.qm_app.common.QmApplication
import com.example.qm_app.components.PullToRefreshColumn
import com.example.qm_app.components.QmCheckbox
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(id: String) {
    val commonViewModel = QmApplication.commonViewModel
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val backStackEntry =
        checkNotNull(QmApplication.navController.currentBackStackEntry) { "The NavController BackStackEntry Is Null" }
    val savedStateHandle = backStackEntry.savedStateHandle

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars,
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
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = {
                                QmApplication.navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "ResponseData",
                                    "Im Ok"
                                )
                                QmApplication.navController.popBackStack()
                            })
                    )
                }
            )
        }
    ) { innerPaddings ->
        val isRefreshing = remember { mutableStateOf(false) }
        val refreshState = rememberPullToRefreshState()
        val coroutineScope = rememberCoroutineScope()

        PullToRefreshColumn(
            isRefreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                coroutineScope.launch {
                    delay(2000)
                    isRefreshing.value = false

                    refreshState.distanceFraction
                }
            },
            threshold = 80.dp,
            refreshState = refreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
        ) {

            val focusManager = LocalFocusManager.current
            focusManager.clearFocus()
            focusManager.moveFocus(FocusDirection.Down)

            val (ref1, ref2) = remember { FocusRequester.createRefs() }

            ref1.requestFocus()
            ref2.freeFocus()

            val scrollState = remember { ScrollState(initial = 0) }

            val context = LocalContext.current
            val tempFile = remember { File(context.externalCacheDir, "temporary_file") }
            val tempUri = remember {
                FileProvider.getUriForFile(
                    context,
                    "com.example.qm_app.FileProvider",
                    tempFile
                )
            }
            val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
            val activityLaunch =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    when (result.resultCode) {
                        RESULT_OK -> {
                            tempUri?.let {
                                imageBitmap.value =
                                    BitmapFactory.decodeStream(
                                        context.contentResolver.openInputStream(
                                            tempUri
                                        )
                                    )
                            }
                        }

                        RESULT_CANCELED -> {}
                    }
                }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Hello World 你好北京 -- $id")
                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(onClick = {
                        commonViewModel.navToMainScreen("home")
                    }) {
                        Text("To Home")
                    }
                    Button(onClick = {
                        commonViewModel.navToMainScreen("cart")
                    }) {
                        Text("To Cart")
                    }
                    Button(onClick = {
                        commonViewModel.navToMainScreen("favorite")
                    }) {
                        Text("To Favorite")
                    }
                    Button(onClick = {
                        savedStateHandle.set("CallbackData", "Hello World")
                        QmApplication.navController.navigate("goods")
                    }) {
                        Text("To Goods")
                    }
                    Button(onClick = {
                        coroutineScope.launch {
                            isRefreshing.value = true
                            delay(5000)
                            isRefreshing.value = false
                        }
                    }) {
                        Text("Force Refresh")
                    }

                    Button(
                        onClick = {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)
                            activityLaunch.launch(intent)
                        }
                    ) {
                        Text("Select Picture")
                    }

                    if (imageBitmap.value != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageBitmap.value)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.background_img),
                            contentDescription = null,
                        )
                    }
                }

                CustomScrollableTabRow()

                val checked = remember { mutableStateOf(false) }
                QmCheckbox(
                    value = checked.value,
                    onChanged = {
                        checked.value = it
                        println("=====================checked: : ${it}")
                    },
                    radius = 4.dp,
                )

                Button(onClick = {
                    checked.value = !checked.value
                }, modifier = Modifier.offset()) {
                    Text(text = if (checked.value) "取消勾选" else "勾选")
                }

            }
        }
    }
}

@Composable
fun CustomScrollableTabRow() {
    val scrollState = rememberScrollState()
    val tabList = listOf("推荐", "科技", "体育", "娱乐", "财经", "汽车", "房产", "教育")
    val tabIndex = remember { mutableIntStateOf(0) }
    val tabItemWidthList = remember { mutableListOf<Int>() }

    LaunchedEffect(tabIndex.value) {
        val offsetX =
            tabItemWidthList.subList(0, tabIndex.value).fold(0) { sum, width -> sum + width }
        val tabWidth = tabItemWidthList[tabIndex.value]
        println("offsetX: $offsetX")
        scrollState.animateScrollTo(
            (offsetX - 540 + tabWidth / 2).coerceIn(
                0,
                scrollState.maxValue
            )
        )
    }

    SubcomposeLayout(
        modifier = Modifier
            .height(50.dp)
            .horizontalScroll(scrollState)
            .background(Color(0xFFCCCCCC))
    ) { constraints ->
        val measurableList = subcompose("items") {

            tabList.forEachIndexed { index, tab ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = { if (tabIndex.value != index) tabIndex.value = index },
                        )
                ) {
                    Text(
                        tab,
                        color = if (tabIndex.value == index) Color(0xFFFF9900) else Color(0xFF999999)
                    )
                }
            }
        }

        val pleasableList = measurableList.map {
            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        val layoutWidth = pleasableList.fold(initial = 0) { curr, measurable ->
            tabItemWidthList.add(measurable.width)
            curr + measurable.width
        }

        layout(layoutWidth, constraints.maxHeight) {
            var x = 0
            pleasableList.forEach { pleasable ->
                pleasable.placeRelative(x, y = (constraints.maxHeight - pleasable.height) / 2)
                x += pleasable.width
            }
        }
    }
}
