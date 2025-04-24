package com.example.datn.models.group

data class CreateRequest(
    val name : String,
    val members : List<String>
)
