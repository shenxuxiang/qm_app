package com.example.qm_app.pages.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.R
import com.example.qm_app.common.QmIcons
import com.example.qm_app.common.RegularPattern
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.components.QmIcon
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.pages.login.components.VerificationCode
import com.example.qm_app.pages.signup.components.InputWidget
import com.example.qm_app.pages.signup.components.RegionWidget
import com.example.qm_app.remember.KeyboardState
import com.example.qm_app.remember.rememberKeyboardState
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.white
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpScreen() {
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }

    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<SignUpViewModel>()
    val (codeRef) = remember { FocusRequester.createRefs() }

    val keyboardState = rememberKeyboardState()

    PageScaffold(
        title = "注册",
        background = painterResource(R.drawable.background_img),
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .fillMaxSize()
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 13.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    // .imeNestedScroll()
                    .clip(corner10)
                    .background(color = white)
                    .padding(horizontal = 13.dp, vertical = 20.dp)
                    .verticalScroll(scrollState)
            ) {
                InputWidget(
                    value = phone,
                    onChange = { phone = it },
                    label = "手机号",
                    placeholder = "请输入手机号",
                    keyboardType = KeyboardType.Phone,
                )
                InputWidget(
                    value = password,
                    onChange = { password = it },
                    label = "密码",
                    placeholder = "请输入密码",
                    modifier = Modifier.padding(top = 20.dp),
                    keyboardType = if (passwordVisibility) KeyboardType.Text else KeyboardType.Password,
                    suffix = {
                        if (password.isNotEmpty())
                            QmIcon(
                                tint = gray,
                                size = if (passwordVisibility) 25.dp else 22.dp,
                                icon = if (passwordVisibility) QmIcons.Stroke.Invisible else QmIcons.Stroke.Visible,
                                modifier = Modifier
                                    .width(26.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = null,
                                        onClick = { passwordVisibility = !passwordVisibility }
                                    )
                            )
                    }
                )
                InputWidget(
                    value = username,
                    onChange = { username = it },
                    label = "用户名",
                    placeholder = "请输入用户名",
                    modifier = Modifier.padding(top = 20.dp),
                )
                RegionWidget(
                    label = "所在地区",
                    placeholder = "请选择所在地区",
                    modifier = Modifier.padding(top = 20.dp),
                )
                InputWidget(
                    value = username,
                    onChange = { username = it },
                    label = "验证码",
                    placeholder = "请输入验证码",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .focusRequester(codeRef),
                    suffix = {
                        VerificationCode(onStart = {
                            Router.navigate(Route.GoodsScreen.route)
                            var result = true
                            if (!RegularPattern.userPhone.matches(phone)) {
                                result = false
                                if (phone.isEmpty()) {
                                    Toast.postShowWarningToast("请填写手机号码")
                                } else {
                                    Toast.postShowWarningToast("您填写的手机号码格式不正确")
                                }
                            } else {
                                coroutineScope.launch {
                                    codeRef.requestFocus()
                                    viewModel.sendSMSAuthCode(phone)
                                }
                            }
                            result
                        })
                    }
                )
            }
            if (keyboardState == KeyboardState.Closed)
                Text(
                    text = "我有账号登录",
                    color = black4,
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                )
        }
    }
}