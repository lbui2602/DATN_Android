package com.example.datn.click

import com.example.datn.models.working_day.WorkingDay
import com.example.datn.models.working_day.WorkingDayXX

interface IClickWorkingDay {
    fun selectWorkingDay(workingDay: WorkingDay)
    fun selectWorkingDay(workingDay: WorkingDayXX)
}