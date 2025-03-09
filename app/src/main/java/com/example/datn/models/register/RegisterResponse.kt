package com.example.datn.models.register

data class RegisterResponse(
    val message: String,
    val code:String,
    val user: User
)