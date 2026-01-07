package com.example.qm_app.pages.login.components

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.qm_app.common.RegularPattern
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.components.alert.Alert
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.pages.login.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun FastLogin(viewModel: LoginViewModel) {
    val context = LocalContext.current
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
                VerificationCode() {
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
        val view = LocalView.current
        ButtonWidget(
            text = "手机号一键登录",
            type = ButtonWidgetType.Default,
            onTap = { showComposeDialog(view, context) },
            modifier = Modifier
                .padding(top = 26.dp)
                .fillMaxWidth()
                .height(36.dp)
        )
    }
}


class ComposeDialog(
    view: View,
    context: Context,
    private val content: @Composable () -> Unit,
) : Dialog(context) {
    init {
        // 设置透明背景（可选）
        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }

//        bottomSheetDialogLayout.setViewTreeLifecycleOwner(view.findViewTreeLifecycleOwner())
//        bottomSheetDialogLayout.setViewTreeViewModelStoreOwner(view.findViewTreeViewModelStoreOwner())
//        bottomSheetDialogLayout.setViewTreeOnBackPressedDispatcherOwner(this)
//        bottomSheetDialogLayout.setViewTreeSavedStateRegistryOwner(
//            composeView.findViewTreeSavedStateRegistryOwner()
//        )

        // 创建 ComposeView 作为内容视图
        // 2. 创建 ComposeView
        val composeView = ComposeView(context).apply {
            // 3. 绑定生命周期所有者
            setViewTreeLifecycleOwner(view.findViewTreeLifecycleOwner())
            setViewTreeViewModelStoreOwner(view.findViewTreeViewModelStoreOwner())
            setViewTreeSavedStateRegistryOwner(view.findViewTreeSavedStateRegistryOwner())

            // 4. 设置内容
            setContent {
                MaterialTheme(
                    colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
                ) {
                    Surface(
                        modifier = Modifier.padding(32.dp),
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 8.dp
                    ) {
                        content()
                    }
                }
            }
        }

        // 设置内容视图
        setContentView(composeView)

        // 设置窗口属性（可选）
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}

// 使用示例
fun showComposeDialog(view: View, context: Context) {
    ComposeDialog(view, context) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("自定义 Dialog 标题", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text("这是在传统 Dialog 中显示的 Compose 内容")
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("确定")
            }
        }
    }.show()
}