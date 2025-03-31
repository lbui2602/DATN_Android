package com.example.datn.models.group

data class Group(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val members: List<String>,
    val name: String,
    val updatedAt: String
)