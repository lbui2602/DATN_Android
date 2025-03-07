package com.example.datn.models.face_api

data class AddFaceResponse(
    val face_count: Int,
    val face_added: Int,
    val request_id: String,
    val outer_id: String
)