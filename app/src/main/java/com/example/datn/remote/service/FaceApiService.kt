package com.example.datn.remote.service

import com.example.datn.models.face_api.AddFaceResponse
import com.example.datn.models.face_api.CompareFaceResponse
import com.example.datn.models.face_api.CreateFaceSetResponse
import com.example.datn.models.face_api.DetectFaceResponse
import com.example.datn.models.face_api.RemoveFaceResponse
import com.example.datn.models.face_api.SearchFaceResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FaceApiService {
    @FormUrlEncoded
    @POST("faceset/create")
    suspend fun createFaceSet(
        @Field("api_key") apiKey: String,
        @Field("api_secret") apiSecret: String,
        @Field("outer_id") outerId: String,
        @Field("display_name") displayName: String? = null,
        @Field("tags") tags: String? = null
    ): CreateFaceSetResponse

    @Multipart
    @POST("detect")
    suspend fun detectFace(
        @Part image: MultipartBody.Part,
        @Part("api_key") apiKey: RequestBody,
        @Part("api_secret") apiSecret: RequestBody
    ): DetectFaceResponse

    @FormUrlEncoded
    @POST("faceset/addface")
    suspend fun addFaceToFaceSet(
        @Field("api_key") apiKey: String,
        @Field("api_secret") apiSecret: String,
        @Field("outer_id") outerId: String,
        @Field("face_tokens") faceTokens: String
    ): AddFaceResponse

    @FormUrlEncoded
    @POST("faceset/removeface")
    suspend fun removeFaceFromFaceSet(
        @Field("api_key") apiKey: String,
        @Field("api_secret") apiSecret: String,
        @Field("outer_id") outerId: String,
        @Field("face_tokens") faceTokens: String
    ): RemoveFaceResponse

    @Multipart
    @POST("compare")
    suspend fun compareFaces(
        @Part image1: MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Query("api_key") apiKey: String,
        @Query("api_secret") apiSecret: String
    ): CompareFaceResponse

    @Multipart
    @POST("search")
    suspend fun searchFace(
        @Part image: MultipartBody.Part,
        @Part("api_key") apiKey: RequestBody,
        @Part("api_secret") apiSecret: RequestBody,
        @Part("outer_id") outerId: RequestBody
    ): SearchFaceResponse
}
