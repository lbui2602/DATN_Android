package com.example.datn.models.department

data class DepartmentsResponses(
    val message : String?,
    val code: String,
    val departments: List<Department>?
)