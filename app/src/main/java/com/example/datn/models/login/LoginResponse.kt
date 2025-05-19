package com.example.datn.models.login

data class LoginResponse(
    val message:String,
    val code:String,
    val _id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val birthday: String,
    val gender: String,
    val roleId: String,
    val idDepartment: String,
    val image: String,
    val status: Boolean,
    val token: String,
    val isAdmin: Boolean,
)