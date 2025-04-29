package com.example.datn.models.group

import android.text.BoringLayout

data class Group(
    val isPrivate : Boolean?,
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val members: List<String>,
    val name: String,
    val image: String?,
    val updatedAt: String
)