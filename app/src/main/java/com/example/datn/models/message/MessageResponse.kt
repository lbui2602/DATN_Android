package com.example.datn.models.message

data class MessageResponse(
    val code: String,
    val message : String,
    val messages: List<Message>
)