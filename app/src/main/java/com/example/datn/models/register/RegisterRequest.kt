package com.example.datn.models.register

data class RegisterRequest(
    val fullname : String,
    val email: String,
    val password: String,
    val phone: String,
    val address: String,
    val departmentId: String,
    val roleId: String)