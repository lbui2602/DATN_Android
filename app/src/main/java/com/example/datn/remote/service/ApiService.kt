package com.example.datn.remote.service

import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.role.RolesResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
}
