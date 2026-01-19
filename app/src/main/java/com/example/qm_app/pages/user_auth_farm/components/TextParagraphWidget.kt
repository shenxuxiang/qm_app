package com.example.qm_app.pages.user_auth_farm.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextParagraphWidget() {
    val style = TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = Color(0xFF4B4B4B)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 12.dp)
    ) {
        Text(
            text = "1、同一个身份证只能认证一个账号；",
            style = style
        )
        Text(
            text = "2、国徽面和正面信息应为同一身份证的信息且在有效期内；",
            style = style
        )
        Text(
            text = "3、所有上传照片需清晰无遮挡，请勿进行美化和修改；",
            style = style
        )
        Text(
            text = "4、上传本人手持身份证信息面照片中应含有本人头部或上半身；",
            style = style
        )
        Text(
            text = "5、手持身份证照片中本人形象需为免冠且未化妆，五官清晰；",
            style = style
        )
        Text(
            text = "6、拍照手持身份证时对焦点请置于身份证上，保证身份证信息清晰和无遮挡；",
            style = style
        )
        Text(
            text = "7、所有上传信息均会被妥善保管，不会用于其他商业用途或传输给其他第三方；",
            style = style
        )
    }
}