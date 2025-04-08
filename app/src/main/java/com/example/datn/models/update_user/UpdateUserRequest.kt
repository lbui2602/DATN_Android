package com.example.datn.models.update_user

data class UpdateUserRequest (
    val fullName : String,
    val phone : String,
    val address : String,
    val roleId : String,
    val idDepartment : String
)