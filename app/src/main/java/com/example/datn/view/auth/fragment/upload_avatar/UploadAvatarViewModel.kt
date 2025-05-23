package com.example.datn.view.auth.fragment.upload_avatar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.BuildConfig
import com.example.datn.models.face_api.AddFaceResponse
import com.example.datn.models.face_api.DetectFaceResponse
import com.example.datn.models.face_token.FaceTokenRequest
import com.example.datn.models.face_token.FaceTokenResponse
import com.example.datn.models.training.TrainingResponse
import com.example.datn.models.upload_avatar.UploadAvatarResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class UploadAvatarViewModel @Inject constructor(
    private val repository: Repository, val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _uploadAvatarResponse = MutableLiveData<TrainingResponse?>()
    val uploadAvatarResponse: LiveData<TrainingResponse?> get() = _uploadAvatarResponse

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun uploadAvatar(userId : RequestBody,image : MultipartBody.Part){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.uploadAvatar(
                    userId,
                    image)
                if(response!=null){
                    _uploadAvatarResponse.postValue(response)
                }
                else{
                    _uploadAvatarResponse.postValue(null)
                }

            } catch (e: Exception) {
                print(e.toString())
                _uploadAvatarResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun training(userId : RequestBody,image : MultipartBody.Part){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.training(
                    userId,
                    image)
                if(response!=null){
                    _uploadAvatarResponse.postValue(response)
                }
                else{
                    _uploadAvatarResponse.postValue(null)
                }

            } catch (e: Exception) {
                print(e.toString())
                _uploadAvatarResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
    fun capturePhoto(context : Context,imageCapture: ImageCapture) {
        _isLoading.value = true
        val file = File(context.externalMediaDirs.first(), "lbui.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    val resizedFile = Util.resizeImageFile(file)
                    uploadImage(file)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                }
            })
    }
    private fun uploadImage(file: File) {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(),sharedPreferencesManager.getUserId().toString())
//        uploadAvatar(userId,body)
        training(userId,body)
    }
}