package com.example.datn.models.mail

data class VerifyOTPRequest(
    val email : String,
    val otp : String
)