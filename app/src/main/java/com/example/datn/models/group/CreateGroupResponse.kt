package com.example.datn.models.group

data class CreateGroupResponse(
    val __v: Int,
    val code: String,
    val message: String?,
    val _id: String,
    val createdAt: String,
    val members: List<String>,
    val name: String,
    val updatedAt: String
)