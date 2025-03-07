package com.example.datn.models.face_api

data class CompareFaceResponse(
    val confidence: Double,
    val request_id: String,
    val thresholds: Thresholds
)