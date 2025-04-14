package com.example.datn.remote.repository

import android.util.Log
import com.example.datn.models.attendance.AttendanceByDateResponse
import com.example.datn.models.attendance.AttendanceByUserIdResponse
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.face_api.AddFaceResponse
import com.example.datn.models.face_api.CompareFaceResponse
import com.example.datn.models.face_api.CreateFaceSetResponse
import com.example.datn.models.face_api.DetectFaceResponse
import com.example.datn.models.face_token.FaceTokenRequest
import com.example.datn.models.face_token.FaceTokenResponse
import com.example.datn.models.group.GroupsResponse
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.message.MessageResponse
import com.example.datn.models.password.ChangePasswordRequest
import com.example.datn.models.password.ChangePasswordResponse
import com.example.datn.models.password.CheckPasswordRequest
import com.example.datn.models.password.CheckPasswordResponse
import com.example.datn.models.profile.ProfileResponse
import com.example.datn.models.profile.User
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.role.RolesResponse
import com.example.datn.models.update_user.UpdateUserRequest
import com.example.datn.models.upload_avatar.UploadAvatarResponse
import com.example.datn.models.working_day.DetailWorkingDayResponse
import com.example.datn.models.working_day.WorkingDay
import com.example.datn.models.working_day.WorkingDayByMonthResponse
import com.example.datn.remote.service.ApiService
import com.example.datn.remote.service.FaceApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService, private val faceApiService: FaceApiService
) {
    suspend fun compareFaces(
        image: MultipartBody.Part,
        faceToken: RequestBody,
        apiKey: RequestBody,
        apiSecret: RequestBody
    ): CompareFaceResponse {
        return faceApiService.compareFaces(image, faceToken, apiKey, apiSecret)
    }

    suspend fun createFaceSet(
        apiKey: String,
        apiSecret: String,
        outerId: String,
        displayName: String,
        tags: String
    ): CreateFaceSetResponse {
        return faceApiService.createFaceSet(apiKey, apiSecret, outerId, displayName, tags)
    }

    suspend fun detectFace(
        image: MultipartBody.Part,
        apiKey: RequestBody,
        apiSecret: RequestBody
    ): DetectFaceResponse {
        return faceApiService.detectFace(image, apiKey, apiSecret)
    }

    suspend fun addFaceToFaceSet(
        apiKey: String,
        apiSecret: String,
        outerId: String,
        faceTokens: String
    ): AddFaceResponse {
        return faceApiService.addFaceToFaceSet(apiKey, apiSecret, outerId, faceTokens)
    }

    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        Log.e("repository", loginRequest.toString())
        return apiService.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return apiService.register(registerRequest)
    }

    suspend fun getRoles(): RolesResponse {
        return apiService.getRoles()
    }

    suspend fun getDepartments(): DepartmentsResponses {
        return apiService.getDepartments()
    }

    suspend fun uploadAvatar(userId: RequestBody, image: MultipartBody.Part): UploadAvatarResponse {
        return apiService.uploadAvatar(userId, image)
    }

    suspend fun updateFaceToken(request: FaceTokenRequest): FaceTokenResponse {
        return apiService.updateFaceToken(request)
    }

    suspend fun attendance(
        token: String,
        userId: RequestBody,
        time: RequestBody,
        date: RequestBody,
        image: MultipartBody.Part
    ): AttendanceResponse {
        return apiService.attendance(token, userId, time, date, image)
    }

    suspend fun getAttendanceByUserIdAndDate(
        token: String,
        request: GetAttendanceByUserIdRequest
    ): AttendanceByDateResponse {
        return apiService.getAttendanceByUserIdAndDate(token, request)
    }

    suspend fun getProfile(token: String): ProfileResponse {
        return apiService.getProfile(token)
    }

    suspend fun changePassword(
        token: String,
        request: ChangePasswordRequest
    ): ChangePasswordResponse {
        return apiService.changePassword(token, request)
    }

    suspend fun getMessages(groupId: String): MessageResponse {
        return apiService.getMessages(groupId)
    }

    suspend fun getGroupByUserId(userId: String): GroupsResponse {
        return apiService.getGroupsByUserId(userId)
    }

    suspend fun getAttendanceByUserId(token: String, userId: String): AttendanceByUserIdResponse {
        return apiService.getAttendanceByUserId(token, userId)
    }

    suspend fun updateUser(token: String, request: UpdateUserRequest): RegisterResponse {
        return apiService.updateUser(token, request)
    }

    suspend fun checkPassword(
        token: String,
        password: CheckPasswordRequest
    ): CheckPasswordResponse {
        return apiService.checkPassword(token, password)
    }

    suspend fun getWorkingDayByUserIdAndMonth(
        token: String,
        userId: String,
        month: String,
        year: String
    ): WorkingDayByMonthResponse {
        return apiService.getWorkingDayByUserIdAndMonth(token, userId, month, year)
    }

    suspend fun getDetailWorkingDay(
        token : String,
        workingDayId:String
    ) : DetailWorkingDayResponse {
        return apiService.getDetailWorkingDay(token,workingDayId)
    }
}