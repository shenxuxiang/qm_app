package com.example.qm_app.pages.user_address

import androidx.compose.runtime.Stable
import com.example.qm_app.entity.UserAddressData

@Stable
data class UiState(
    val total: Int = 0,
    val pageNum: Int = 1,
    val pageSize: Int = 3,
    val defaultAddress: UserAddressData? = null, // 默认地址
    val addressList: List<UserAddressData> = ArrayList(),
)