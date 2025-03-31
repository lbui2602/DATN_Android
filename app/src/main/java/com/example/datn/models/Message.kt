package com.example.datn.models

data class Message(
    val _id: String,
    val groupId: String,
    val senderName: String,
    val message: String,
    val createdAt: String,
    val updatedAt: String
)