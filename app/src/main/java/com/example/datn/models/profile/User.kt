package com.example.datn.models.profile

data class User(
    val _id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val role: String,
    val department: String,
    val image: String,
    val face_token: String,
    val status : Boolean,
    val createAt: String,
    val updateAt : String,
    val __v : Int
)
