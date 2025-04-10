package com.example.datn.models.working_day

data class WorkingDayByMonthResponse(
    val code: String,
    val workingDays: List<WorkingDay>
)