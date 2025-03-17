package com.example.datn.remote.service

import com.example.datn.models.upload_avatar.UploadAvatarResponse
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.role.RolesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request : RegisterRequest
    ): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/roles")
    suspend fun getRoles() : RolesResponse

    @GET("api/departments")
    suspend fun getDepartments() : DepartmentsResponses

    @Multipart
    @POST("api/uploadAvatar/{userId}")
    fun uploadAvatar(
        @Path("userId") userId: RequestBody,
        @Part image: MultipartBody.Part
    ): UploadAvatarResponse
}
