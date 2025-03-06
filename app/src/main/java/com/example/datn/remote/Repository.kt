package com.example.datn.remote

import android.util.Log
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.remote.service.ApiService
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(loginRequest: LoginRequest) : LoginResponse {
        Log.e("repository",loginRequest.toString())
        return apiService.login(loginRequest)
    }
}