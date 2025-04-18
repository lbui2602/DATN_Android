package com.example.datn.models.user

import com.example.datn.models.register.User

data class UserResponse(
    val code: String,
    val message: String,
    val users: List<User>?
)