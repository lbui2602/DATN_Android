package com.example.datn.models.face_api

data class RemoveFaceResponse(
    val face_removed: Int,
    val face_count: Int,
    val request_id: String
)