package com.example.qm_app.pages.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.qm_app.components.Alert
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.pages.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FastLogin(viewModel: LoginViewModel) {
    val view = LocalView.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    var code by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var checked by rememberSaveable { mutableStateOf(false) }
    val (phoneRef, codeRef) = remember { FocusRequester.createRefs() }
    val loginButtonEnabled by derivedStateOf { code.isNotEmpty() && phone.isNotEmpty() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 34.dp, end = 34.dp, top = 40.dp)
    ) {
        InputBox(
            value = phone,
            onChange = { phone = it },
            placeholder = "请输入手机号",
            keyboardType = KeyboardType.Phone,
            modifier = Modifier.focusRequester(phoneRef),
        )
        InputBox(
            value = code,
            onChange = { code = it },
            placeholder = "请输入验证码",
            keyboardType = KeyboardType.Number,
            modifier = Modifier
                .padding(top = 26.dp)
                .focusRequester(codeRef),
            suffix = {
                VerificationCode {
                    var result = true
                    if (!RegularPattern.userPhone.matches(phone)) result = false

                    coroutineScope.launch {
                        if (phone.isEmpty()) {
                            Toast.showWarningToast("请输入手机号码")
                        } else if (!RegularPattern.userPhone.matches(input = phone)) {
                            Toast.showWarningToast("号码格式不正确")
                        } else {
                            codeRef.requestFocus()
                            viewModel.sendSMSAuthCode(phone = phone, type = 1)
                        }
                    }

                    return@VerificationCode result
                }
            }
        )

        ButtonWidget(
            onTap = {
                focusManager.clearFocus()
                if (!RegularPattern.userPhone.matches(phone)) {
                    Toast.postShowWarningToast("请输入正确的手机号码")
                    return@ButtonWidget
                } else if (!RegularPattern.verificationCode.matches(code)) {
                    Toast.postShowWarningToast("请输入正确的验证码")
                    return@ButtonWidget
                }
                if (!checked) {
                    Alert.confirm(
                        view = view,
                        text = "请您阅读并同意《用户协议》和《隐私协议》",
                        onConfirm = { checked = true },
                        confirmText = "同意"
                    )
                    return@ButtonWidget
                }

                coroutineScope.launch {
                    Loading.show()
                    viewModel.authLoginForPhoneCode(phone, code)
                    Loading.hide()
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
            checked,
            onChange = { checked = it },
            modifier = Modifier.padding(top = 14.dp),
        )

        DividerWidget(label = "其他登录方式")
        ButtonWidget(
            text = "手机号一键登录",
            type = ButtonWidgetType.Default,
            onTap = {
                coroutineScope.launch {
                    Toast.postShowWarningToast("未知异常")
                    delay(500)
                    Loading.postShow()
                    delay(3000)
                    Loading.postHide()
                }
            },
            modifier = Modifier
                .padding(top = 26.dp)
                .fillMaxWidth()
                .height(36.dp)
        )
    }
}


