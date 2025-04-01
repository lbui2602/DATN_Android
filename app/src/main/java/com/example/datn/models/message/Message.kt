package com.example.datn.models.message

data class Message(
    val _id: String,
    val groupId: String,
    val message: String,
    val senderId: String,
    val senderImage: String,
    val senderName: String,
    val createdAt: String,
    val updatedAt: String
)