package com.example.datn.models.register

import com.example.datn.models.department.Department

data class RegisterRequest(
    val fullName : String,
    val email: String,
    val password: String,
    val phone: String,
    val address: String,
    val birthday : String,
    val gender : String,
    val idDepartment: String,
    val roleId: String)