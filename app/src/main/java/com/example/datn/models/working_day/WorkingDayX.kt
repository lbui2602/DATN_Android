package com.example.datn.models.working_day

import com.example.datn.models.attendance.Attendance

data class WorkingDayX(
    val __v: Int,
    val _id: String,
    val attendances: List<Attendance>,
    val date: String,
    val status: Int,
    val totalHours: Int,
    val userId: UserId
)