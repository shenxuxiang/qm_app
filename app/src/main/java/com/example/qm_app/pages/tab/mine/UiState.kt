package com.example.qm_app.pages.tab.mine

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.example.qm_app.R
import com.example.qm_app.entity.UserCheckStatus

data class MineMenuItem(@field:DrawableRes val icon: Int, val label: String, val link: Any)

@Stable
data class UiState(
    val userCheckStatus: UserCheckStatus? = null,
    val mineServiceMenus: List<MineMenuItem> = listOf(
        MineMenuItem(label = "我的菜单", icon = R.drawable.mine_service_1, link = ""),
        MineMenuItem(label = "我的需求", icon = R.drawable.mine_service_2, link = ""),
        MineMenuItem(label = "我的提问", icon = R.drawable.mine_service_3, link = ""),
        MineMenuItem(label = "我的农场", icon = R.drawable.mine_service_4, link = ""),
        MineMenuItem(label = "我的贷款", icon = R.drawable.mine_service_5, link = ""),
        MineMenuItem(label = "我的保险", icon = R.drawable.mine_service_6, link = ""),
        MineMenuItem(label = "我的奖补", icon = R.drawable.mine_service_7, link = ""),
        MineMenuItem(label = "客服热线", icon = R.drawable.mine_service_8, link = ""),
    ),
    val mineSettingsMenus: List<MineMenuItem> = listOf(
        MineMenuItem(label = "个人资料", icon = R.drawable.mine_settings_1, link = ""),
        MineMenuItem(label = "地址管理", icon = R.drawable.mine_settings_2, link = ""),
        MineMenuItem(label = "更新记录", icon = R.drawable.mine_settings_3, link = ""),
        MineMenuItem(label = "检查更新", icon = R.drawable.mine_settings_4, link = ""),
        MineMenuItem(label = "清除缓存", icon = R.drawable.mine_settings_5, link = ""),
        MineMenuItem(label = "关于我们", icon = R.drawable.mine_settings_6, link = ""),
        MineMenuItem(label = "退出登录", icon = R.drawable.mine_settings_7, link = ""),
    ),
)