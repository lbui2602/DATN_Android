package com.example.datn.models.working_day

data class WorkingDay(
    val __v: Int,
    val _id: String,
    val attendances: List<Attendance>,
    val date: String,
    val status: Int,
    val totalHours: Double,
    val userId: String
)