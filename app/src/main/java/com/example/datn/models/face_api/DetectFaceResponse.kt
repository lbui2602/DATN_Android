package com.example.datn.models.face_api

data class DetectFaceResponse(
    val request_id: String,
    val faces: List<FaceInfo>
)