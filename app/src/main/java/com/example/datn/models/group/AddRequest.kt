package com.example.datn.models.group

data class AddRequest(
    val groupId : String,
    val userIds : List<String>
)