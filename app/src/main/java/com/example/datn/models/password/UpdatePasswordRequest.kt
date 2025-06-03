package com.example.datn.models.password

data class UpdatePasswordRequest(
    val email : String,
    val newPassword : String,
)