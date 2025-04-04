package com.example.datn.models.attendance

data class AttendanceByUserIdResponse(
    val code: String,
    val attendances: List<Attendance>?,
    val message: String?
)