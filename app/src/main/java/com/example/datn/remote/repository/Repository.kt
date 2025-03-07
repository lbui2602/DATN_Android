package com.example.datn.remote.repository

import android.util.Log
import com.example.datn.models.face_api.CreateFaceSetResponse
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.remote.service.ApiService
import com.example.datn.remote.service.FaceApiService
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService, private val faceApiService: FaceApiService
) {
    suspend fun login(loginRequest: LoginRequest) : LoginResponse {
        Log.e("repository",loginRequest.toString())
        return apiService.login(loginRequest)
    }
    suspend fun createFaceSet(apiKey: String,apiSecret:String,outerId:String,displayName : String,tags : String) : CreateFaceSetResponse{
        return faceApiService.createFaceSet(apiKey, apiSecret, outerId, displayName, tags)
    }
}