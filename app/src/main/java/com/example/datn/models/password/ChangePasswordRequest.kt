package com.example.datn.models.password

data class ChangePasswordRequest(
    val oldPassword : String,
    val newPassword : String
)