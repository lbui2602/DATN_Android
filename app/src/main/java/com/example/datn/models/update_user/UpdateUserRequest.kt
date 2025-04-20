package com.example.datn.models.update_user

data class UpdateUserRequest (
    val fullName : String?,
    val phone : String?,
    val address : String?,
    val birthday : String?,
    val gender : String?,
    val roleId : String,
    val idDepartment : String
)