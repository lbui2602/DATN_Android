package com.example.datn.models.login

data class LoginResponse(
    val _id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val roleId: String,
    val idDepartment: String,
    val image: String,
    val token: String
)