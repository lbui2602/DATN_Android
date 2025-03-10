package com.example.datn.models.role

data class RolesResponse(
    val code: String,
    val message: String,
    val roles: List<Role>
)