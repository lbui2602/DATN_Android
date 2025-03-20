package com.example.datn.models.attendance

data class AttendanceByDateResponse(
    val code: String,
    val attendances: List<Attendance>?,
    val totalHours: Double?,
    val message: String?
)
