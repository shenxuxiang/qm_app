package com.example.qm_app.pages.user_auth_farm_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qm_app.api.UserAuthServiceApi
import com.example.qm_app.common.LogUntil
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthFarmViewViewModel @Inject constructor() : ViewModel() {
    private val serviceApi = HttpRequest.create<UserAuthServiceApi>()
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    /**
     * 获取农夫认证信息
     * */
    fun queryFarmCheckInfo() {
        viewModelScope.launch {
            try {
                val resp = serviceApi.queryFarmCheckInfo(mapOf("1" to "1")).await()
                val data = resp.data
                _uiState.update {
                    it.copy(
                        loading = false,
                        status = data.status,
                        realName = data.realName,
                        idCardNum = data.idCardNum,
                        validDate = data.validDate,
                        checkRemark = data.checkRemark,
                        portraitUrl = data.portraitUrl,
                        handHeldUrl = data.handHeldUrl,
                        userTypeName = data.userTypeName,
                        checkStatusName = data.checkStatusName,
                        nationalEmblemUrl = data.nationalEmblemUrl,
                    )
                }
            } catch (exception: Exception) {
                LogUntil.d(exception)
            }
        }
    }

    init {
        queryFarmCheckInfo()
    }
}