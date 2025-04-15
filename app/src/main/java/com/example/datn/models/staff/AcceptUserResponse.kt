package com.example.datn.models.staff

import com.example.datn.models.register.User

data class AcceptUserResponse(
    val message: String,
    val code: String,
    val user: User?
)