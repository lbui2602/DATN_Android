package com.example.datn.models.attendance

import com.example.datn.models.department.Department

data class AttendanceRequest(
    var name: String?,
    var date: String?,
    var idDepartment: String?
)