package com.example.datn.models.staff

data class User(
    val _id: String,
    val address: String,
    val birthday: String,
    val gender: String,
    val createdAt: String,
    val department: String,
    val email: String,
    val face_token: String,
    val fullName: String,
    val image: String,
    val phone: String,
    val role: String,
    val status: Boolean,
    val updatedAt: String
)