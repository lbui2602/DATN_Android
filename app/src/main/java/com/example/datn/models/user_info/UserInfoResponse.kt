package com.example.datn.models.user_info

data class UserInfoResponse(
    val message:String,
    val code:String,
    val user : User
)