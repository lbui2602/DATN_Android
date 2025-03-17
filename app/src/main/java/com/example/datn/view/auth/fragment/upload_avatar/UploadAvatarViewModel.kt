package com.example.datn.view.auth.fragment.upload_avatar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.face_api.AddFaceResponse
import com.example.datn.models.face_api.DetectFaceResponse
import com.example.datn.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadAvatarViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _detectFaceResponse = MutableLiveData<DetectFaceResponse?>()
    val detectFaceResponse: LiveData<DetectFaceResponse?> get() = _detectFaceResponse

    private val _addFaceResponse = MutableLiveData<AddFaceResponse?>()
    val addFaceResponse: LiveData<AddFaceResponse?> get() = _addFaceResponse

    fun detectFace(image: MultipartBody.Part, apiKey: RequestBody, apiSecret: RequestBody){
        viewModelScope.launch {
            try {
                val response = repository.detectFace(image,apiKey,apiSecret)
                if(response!=null){
                    _detectFaceResponse.postValue(response)
                }else{
                    _detectFaceResponse.postValue(null)
                }

            } catch (e: Exception) {
                print(e.toString())
                _detectFaceResponse.postValue(null)
            }
        }
    }
    fun addFaceToFaceSet(apiKey: String,apiSecret:String,outerId:String,faceTokens:String){
        viewModelScope.launch {
            try {
                val response = repository.addFaceToFaceSet(apiKey,apiSecret,outerId,faceTokens)
                if(response!=null){
                    _addFaceResponse.postValue(response)
                }
                else{
                    _addFaceResponse.postValue(null)
                }

            } catch (e: Exception) {
                print(e.toString())
                _addFaceResponse.postValue(null)
            }
        }
    }
}