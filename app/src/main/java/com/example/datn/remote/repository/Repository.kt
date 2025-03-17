package com.example.datn.remote.repository

import android.util.Log
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.face_api.AddFaceResponse
import com.example.datn.models.face_api.CreateFaceSetResponse
import com.example.datn.models.face_api.DetectFaceResponse
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.role.RolesResponse
import com.example.datn.models.upload_avatar.UploadAvatarResponse
import com.example.datn.remote.service.ApiService
import com.example.datn.remote.service.FaceApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService, private val faceApiService: FaceApiService
) {
    suspend fun login(loginRequest: LoginRequest) : LoginResponse {
        Log.e("repository",loginRequest.toString())
        return apiService.login(loginRequest)
    }
    suspend fun register(registerRequest: RegisterRequest) : RegisterResponse {
        return apiService.register(registerRequest)
    }
    suspend fun createFaceSet(apiKey: String,apiSecret:String,outerId:String,displayName : String,tags : String) : CreateFaceSetResponse{
        return faceApiService.createFaceSet(apiKey, apiSecret, outerId, displayName, tags)
    }
    suspend fun detectFace(image: MultipartBody.Part, apiKey: RequestBody, apiSecret: RequestBody) : DetectFaceResponse {
        return faceApiService.detectFace(image,apiKey, apiSecret)
    }
    suspend fun addFaceToFaceSet(apiKey: String,apiSecret:String,outerId:String,faceTokens:String) : AddFaceResponse {
        return faceApiService.addFaceToFaceSet(apiKey, apiSecret, outerId, faceTokens)
    }
    suspend fun getRoles() : RolesResponse {
        return apiService.getRoles()
    }
    suspend fun getDepartments() : DepartmentsResponses {
        return apiService.getDepartments()
    }
    suspend fun uploadAvatar(userId : RequestBody, image : MultipartBody.Part) : UploadAvatarResponse{
        return apiService.uploadAvatar(userId,image)
    }
}