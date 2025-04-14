package com.example.datn.models.working_day

data class DetailWorkingDayResponse(
    val message : String?,
    val code: String,
    val workingDay: WorkingDayX
)