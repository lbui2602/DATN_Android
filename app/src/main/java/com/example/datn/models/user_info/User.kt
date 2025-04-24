package com.example.datn.models.user_info

data class User(
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
    val status : Boolean,
    val createAt: String,
    val updateAt : String,
    val __v : Int
)