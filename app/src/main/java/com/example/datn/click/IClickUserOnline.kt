package com.example.datn.click

import com.example.datn.models.register.User

interface IClickUserOnline {
    fun clickUser(user : User)
    fun selectUser(user : User, isCheck : Boolean)
}