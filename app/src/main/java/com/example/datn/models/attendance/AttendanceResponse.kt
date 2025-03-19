package com.example.datn.models.attendance

data class AttendanceResponse(
    val attendance: Attendance,
    val attendances: List<Attendance>,
    val code: String,
    val message: String,
    val totalHours: Int
)