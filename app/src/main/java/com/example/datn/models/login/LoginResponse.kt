package com.example.datn.models.login

data class LoginResponse(
    val message:String,
    val code:String,
    val _id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val roleId: String,
    val idDepartment: String,
    val image: String,
    val face_token: String,
    val token: String
)