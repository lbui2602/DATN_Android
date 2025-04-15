package com.example.datn.click

import com.example.datn.models.staff.User

interface IClickUser {
    fun clickUser(user: User)
    fun confirmUser(user: User)
}