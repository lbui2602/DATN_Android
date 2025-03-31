package com.example.datn.models.group

data class GroupsResponse(
    val code: String,
    val groups: List<Group>,
    val message: String
)