package com.example.datn.models.attendance

data class GetAllAttendanceResponse(
    val attendances: List<AttendanceX>,
    val code: String
)