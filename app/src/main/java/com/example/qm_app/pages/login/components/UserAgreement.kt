package com.example.qm_app.pages.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.components.QmCheckbox
import com.example.qm_app.ui.theme.black6
import com.example.qm_app.ui.theme.primaryColor

@Composable
fun UserAgreement(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onChange: (checked: Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.then(Modifier.fillMaxWidth()),
    ) {
        QmCheckbox(size = 16.dp, radius = 2.dp, value = checked, onChange = onChange)
        Row(modifier = Modifier.padding(start = 6.dp)) {
            Text("登录代表您已同意", fontSize = 10.sp, lineHeight = 10.sp, color = black6)
            Text("《用户协议》", fontSize = 10.sp, lineHeight = 10.sp, color = primaryColor)
            Text("和", fontSize = 10.sp, lineHeight = 10.sp, color = black6)
            Text("《隐私协议》", fontSize = 10.sp, lineHeight = 10.sp, color = primaryColor)
        }
    }
}