package com.example.qm_app.pages.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.qm_app.common.QmApplication
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.components.QmIcon
import com.example.qm_app.router.Route
import com.example.qm_app.ui.theme.gray

@Composable
fun AccountLogin() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 34.dp, end = 34.dp, top = 40.dp)
    ) {
        val focusManager = LocalFocusManager.current
        var account by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var checked by rememberSaveable { mutableStateOf(false) }
        var passwordVisibility by remember { mutableStateOf(false) }
        val (accountRef, passwordRef) = remember { FocusRequester.createRefs() }
        val loginButtonEnabled by derivedStateOf { account.isNotEmpty() && password.isNotEmpty() }

        InputBox(
            value = account,
            onChange = { account = it },
            placeholder = "请输入账号",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.focusRequester(accountRef),
        )
        InputBox(
            value = password,
            onChange = { password = it },
            placeholder = "请输入密码",
            keyboardType = if (passwordVisibility) KeyboardType.Text else KeyboardType.Password,
            modifier = Modifier
                .padding(top = 26.dp)
                .focusRequester(passwordRef),
            suffix = {
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
            onTap = {},
            disabled = !loginButtonEnabled,
            type = ButtonWidgetType.Primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(36.dp)
        )
        UserAgreement(
            checked,
            onChange = { checked = it },
            modifier = Modifier.padding(top = 14.dp),
        )
        DividerWidget(label = "注册新账号")
        ButtonWidget(
            text = "注册",
            type = ButtonWidgetType.Default,
            onTap = { QmApplication.navController.navigate(Route.SignUpScreen.route) },
            modifier = Modifier
                .padding(top = 26.dp)
                .fillMaxWidth()
                .height(36.dp)
        )
    }
}