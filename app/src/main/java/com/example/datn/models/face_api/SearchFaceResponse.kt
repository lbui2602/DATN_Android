package com.example.datn.models.face_api

data class SearchFaceResponse(
    val request_id: String,
    val faces: List<SearchResult>
)