package com.example.datn.remote.service

import com.example.datn.Response
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("api/auth/register")
    fun uploadImage(
        @Part("fullName") fullName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("address") address: RequestBody,
        @Part("roleId") roleId: RequestBody,
        @Part("idDepartment") idDepartment: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Response>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
