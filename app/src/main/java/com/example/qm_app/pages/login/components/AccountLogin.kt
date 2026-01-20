package com.example.qm_app.pages.login.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.qm_app.common.Encrypter
import com.example.qm_app.common.QmIcons
import com.example.qm_app.common.RegularPattern
import com.example.qm_app.common.TokenManager
import com.example.qm_app.common.UserManager
import com.example.qm_app.components.Alert
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.components.QmIcon
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.pages.login.LoginViewModel
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.gray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun AccountLogin(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 34.dp, end = 34.dp, top = 40.dp)
    ) {
        val anchorView = LocalView.current
        val focusManager = LocalFocusManager.current
        val coroutineScope = rememberCoroutineScope()
        val uiState by viewModel.uiState.collectAsState()
        var passwordVisibility by rememberSaveable { mutableStateOf(false) }
        val (accountRef, passwordRef) = remember { FocusRequester.createRefs() }
        val loginButtonEnabled by derivedStateOf { uiState.account.isNotEmpty() && uiState.password.isNotEmpty() }

        InputBox(
            value = uiState.account,
            placeholder = "请输入账号",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.focusRequester(accountRef),
            onChange = { input -> viewModel.updateUIState { it.copy(account = input) } },
        )
        InputBox(
            value = uiState.password,
            placeholder = "请输入密码",
            onChange = { input -> viewModel.updateUIState { it.copy(password = input) } },
            keyboardType = if (passwordVisibility) KeyboardType.Text else KeyboardType.Password,
            modifier = Modifier
                .padding(top = 26.dp)
                .focusRequester(passwordRef),
            suffix = {
                if (uiState.password.isNotEmpty())
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
        ButtonWidget(
            text = "登录",
            onTap = {
                focusManager.clearFocus(true)
                if (!RegularPattern.password.matches(uiState.password)) {
                    Toast.postShowWarningToast("密码格式不正确")
                    return@ButtonWidget
                } else if (!uiState.checkedOfAccount) {
                    Alert.confirm(
                        view = anchorView,
                        confirmText = "同意",
                        text = "请您阅读并同意《用户协议》和《隐私协议》",
                        onConfirm = { viewModel.updateUIState { it.copy(checkedOfAccount = true) } },
                    )
                    return@ButtonWidget
                }

                coroutineScope.launch {
                    Loading.show()
                    val resp = viewModel.authLogin(
                        account = uiState.account,
                        password = Encrypter.encrypt(uiState.password),
                    )
                    Loading.hide()
                    delay(400)
                    if (resp != null) {
                        Toast.showSuccessToast("登录成功")
                        UserManager.updateUserInfo(resp.data)
                        TokenManager.token = resp.data["token"] as String
                        Router.navigate(Route.MainScreen.route) {
                            popUpTo(Route.LoginScreen.route) { inclusive = true }
                        }
                    }
                }
            },
            disabled = !loginButtonEnabled,
            type = ButtonWidgetType.Primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(36.dp)
        )
        UserAgreement(
            checked = uiState.checkedOfAccount,
            modifier = Modifier.padding(top = 14.dp),
            onChange = { checked -> viewModel.updateUIState { it.copy(checkedOfAccount = checked) } },
        )
        DividerWidget(label = "注册新账号")
        ButtonWidget(
            text = "注册",
            type = ButtonWidgetType.Default,
            onTap = { Router.navigate(Route.SignUpScreen.route) },
            modifier = Modifier
                .padding(top = 26.dp)
                .fillMaxWidth()
                .height(36.dp)
        )
    }
}