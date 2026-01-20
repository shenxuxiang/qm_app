package com.example.qm_app.pages.user_auth_farm

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qm_app.api.UserAuthServiceApi
import com.example.qm_app.common.LogUntil
import com.example.qm_app.common.QmApplication
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor() : ViewModel() {
    private val serviceApi = HttpRequest.create<UserAuthServiceApi>()

    private val _uiState = MutableStateFlow(UiState())

    val uiState = _uiState.asStateFlow()

    fun updateUIState(block: (UiState) -> UiState) {
        _uiState.update { block(it) }
    }

    private fun getFormData(file: File, bitmap: Bitmap): MultipartBody.Part {
        FileOutputStream(file).use {
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                it,
            )
        }

        val requestBody = file.asRequestBody(null)
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }

    /**
     * 身份证背面照片上传（国徽面）
     * */
    fun uploadIDCardBackFace(bitmap: Bitmap) {
        viewModelScope.launch {
            Loading.show("正在上传，请稍等~")
            val tempFile = File(
                QmApplication.context.externalCacheDir,
                "upload_file_user_id_card_back_face.jpeg"
            )
            try {
                val formData = getFormData(tempFile, bitmap)
                val resp = serviceApi.uploadIDCardBackFace(formData).await()
                // 更新信息
                updateUIState {
                    it.copy(
                        url1 = resp.data.url,
                        validDate = resp.data.validDate,
                    )
                }
            } catch (exception: Exception) {
                LogUntil.d(exception)
            } finally {
                tempFile.delete()
                Loading.hide()
            }
        }
    }

    /**
     * 身份证正面照片上传（人像面）
     * */
    fun uploadIDCardFrontFace(bitmap: Bitmap) {
        viewModelScope.launch {
            Loading.show("正在上传，请稍等~")
            val tempFile = File(
                QmApplication.context.externalCacheDir,
                "upload_file_user_id_card_front_face.jpeg"
            )
            try {
                val formData = getFormData(tempFile, bitmap)
                val resp = serviceApi.uploadIDCardFrontFace(formData).await()
                // 更新信息
                updateUIState {
                    it.copy(
                        url2 = resp.data.url,
                        userName = resp.data.name,
                        idNumber = resp.data.idCardNum,
                    )
                }
            } catch (exception: Exception) {
                LogUntil.d(exception)
            } finally {
                tempFile.delete()
                Loading.hide()
            }
        }
    }

    /**
     * 上传第三张照片
     * */
    fun uploadPhotoOfThird(bitmap: Bitmap) {
        viewModelScope.launch {
            Loading.show("正在上传，请稍等~")
            val tempFile = File(
                QmApplication.context.externalCacheDir,
                "upload_file_user_photo_of_third.jpeg"
            )
            try {
                val formData = getFormData(tempFile, bitmap)
                val resp = serviceApi.uploadFile(formData).await()
                // 更新信息
                updateUIState { it.copy(url3 = resp.data["path"] as String) }
            } catch (exception: Exception) {
                LogUntil.d(exception)
            } finally {
                tempFile.delete()
                Loading.hide()
            }
        }
    }

    fun submitFarmAuth(query: Map<String, String>) {
        viewModelScope.launch {
            var isOk = false
            try {
                Loading.show("正在提交，请稍等~")
                serviceApi.submitFarmAuth(query).await()
                isOk = true
            } catch (exception: Exception) {
                LogUntil.d(exception)
            } finally {
                Loading.hide()
                delay(400)
                if (isOk) Toast.showSuccessToast("提交成功~")
                delay(1000)
                Router.popBackStack(Route.MainScreen.route, inclusive = false, saveState = false)
            }
        }
    }
}