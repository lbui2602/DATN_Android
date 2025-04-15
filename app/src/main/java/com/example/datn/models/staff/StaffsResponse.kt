package com.example.datn.models.staff

data class StaffsResponse(
    val message : String?,
    val code: String,
    val users: List<User>
)