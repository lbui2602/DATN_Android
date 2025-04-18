package com.example.datn.models.register

data class User(
    val __v: Int,
    val _id: String,
    val address: String,
    val createdAt: String,
    val email: String,
    val fullName: String,
    val idDepartment: String,
    val image: String,
    val password: String,
    val phone: String,
    val roleId: String,
    val updatedAt: String,
    val isOnline : Boolean?
)