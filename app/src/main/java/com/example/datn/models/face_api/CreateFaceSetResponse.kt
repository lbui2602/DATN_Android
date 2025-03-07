package com.example.datn.models.face_api

data class CreateFaceSetResponse(
    val faceset_token: String,
    val outer_id: String,
    val face_count: Int,
    val request_id: String
)
