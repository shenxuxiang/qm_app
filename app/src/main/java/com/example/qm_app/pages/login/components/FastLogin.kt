package com.example.qm_app.pages.login.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.qm_app.common.RegularPattern
import com.example.qm_app.common.TokenManager
import com.example.qm_app.common.UserManager
import com.example.qm_app.components.Alert
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.pages.login.LoginViewModel
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun FastLogin(viewModel: LoginViewModel) {
    val view = LocalView.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val (phoneRef, codeRef) = remember { FocusRequester.createRefs() }
    val loginButtonEnabled by derivedStateOf { uiState.code.isNotEmpty() && uiState.phone.isNotEmpty() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 34.dp, end = 34.dp, top = 40.dp)
    ) {
        InputBox(
            value = uiState.phone,
            placeholder = "请输入手机号",
            keyboardType = KeyboardType.Phone,
            modifier = Modifier.focusRequester(phoneRef),
            onChange = { input -> viewModel.updateUIState { it.copy(phone = input) } },
        )
        InputBox(
            value = uiState.code,
            onChange = { input -> viewModel.updateUIState { it.copy(code = input) } },
            placeholder = "请输入验证码",
            keyboardType = KeyboardType.Number,
            modifier = Modifier
                .padding(top = 26.dp)
                .focusRequester(codeRef),
            suffix = {
                VerificationCode {
                    var result = true
                    if (!RegularPattern.userPhone.matches(uiState.phone)) result = false

                    if (uiState.phone.isEmpty()) {
                        Toast.showWarningToast("请输入手机号码")
                    } else if (!RegularPattern.userPhone.matches(input = uiState.phone)) {
                        Toast.showWarningToast("号码格式不正确")
                    } else {
                        codeRef.requestFocus()
                        viewModel.sendSMSAuthCode(phone = uiState.phone, type = 1)
                    }

                    return@VerificationCode result
                }
            }
        )

        ButtonWidget(
            onTap = {
                focusManager.clearFocus()
                if (!RegularPattern.userPhone.matches(uiState.phone)) {
                    Toast.postShowWarningToast("请输入正确的手机号码")
                    return@ButtonWidget
                } else if (!RegularPattern.verificationCode.matches(uiState.code)) {
                    Toast.postShowWarningToast("请输入正确的验证码")
                    return@ButtonWidget
                }
                if (!uiState.checkedOfFast) {
                    Alert.confirm(
                        view = view,
                        confirmText = "同意",
                        text = "请您阅读并同意《用户协议》和《隐私协议》",
                        onConfirm = { viewModel.updateUIState { it.copy(checkedOfFast = true) } },
                    )
                    return@ButtonWidget
                }

                coroutineScope.launch {
                    Loading.show()
                    val resp = viewModel.authLoginForPhoneCode(uiState.phone, uiState.code)
                    Loading.hide()
                    delay(400)
                    if (resp != null) {
                        Toast.showSuccessToast("登录成功")
                        UserManager.updateUserInfo(resp.data)
                        TokenManager.token = resp.data["token"] as String
                        Router.navigate(Route.HomeScreen.route) {
                            popUpTo(Route.LoginScreen.route) { inclusive = true }
                        }
                    }
                }
            },
            text = "登录",
            disabled = !loginButtonEnabled,
            type = ButtonWidgetType.Primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(height = 36.dp),
        )

        UserAgreement(
            checked = uiState.checkedOfFast,
            modifier = Modifier.padding(top = 14.dp),
            onChange = { checked -> viewModel.updateUIState { it.copy(checkedOfFast = checked) } },
        )

        DividerWidget(label = "其他登录方式")
        ButtonWidget(
            text = "手机号一键登录",
            type = ButtonWidgetType.Default,
            onTap = {},
            modifier = Modifier
                .padding(top = 26.dp)
                .fillMaxWidth()
                .height(36.dp)
        )
    }
}


