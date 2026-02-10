package com.example.qm_app.entity

data class UserAddressData(
    val phone: String,
    val userId: String,
    val address: String,
    val username: String,
    val addressId: String,
    val regionCode: String,
    val regionName: String,
    var defaultFlag: Boolean,
)