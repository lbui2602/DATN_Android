package com.example.datn.remote.service

import com.example.datn.models.attendance.AttendanceByDateResponse
import com.example.datn.models.attendance.AttendanceByUserIdResponse
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.models.upload_avatar.UploadAvatarResponse
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.face_token.FaceTokenRequest
import com.example.datn.models.face_token.FaceTokenResponse
import com.example.datn.models.group.GroupsResponse
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.message.MessageResponse
import com.example.datn.models.password.ChangePasswordRequest
import com.example.datn.models.password.ChangePasswordResponse
import com.example.datn.models.password.CheckPasswordRequest
import com.example.datn.models.password.CheckPasswordResponse
import com.example.datn.models.profile.ProfileResponse
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.role.RolesResponse
import com.example.datn.models.staff.AcceptUserRequest
import com.example.datn.models.staff.AcceptUserResponse
import com.example.datn.models.staff.StaffsResponse
import com.example.datn.models.update_user.UpdateUserRequest
import com.example.datn.models.working_day.DetailWorkingDayResponse
import com.example.datn.models.working_day.WorkingDayByMonthResponse
import com.example.datn.view.main.fragment.working_day.DetailWorkingDayViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //auth
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @Multipart
    @POST("api/auth/uploadAvatar")
    suspend fun uploadAvatar(
        @Part("userId") userId: RequestBody,
        @Part image: MultipartBody.Part
    ): UploadAvatarResponse

    @PUT("api/auth/updateFaceToken")
    suspend fun updateFaceToken(
        @Body request: FaceTokenRequest
    ): FaceTokenResponse

    //system
    @GET("api/roles")
    suspend fun getRoles(): RolesResponse

    @GET("api/departments")
    suspend fun getDepartments(): DepartmentsResponses

    @Multipart
    @POST("api/attendance")
    suspend fun attendance(
        @Header("Authorization") token: String,
        @Part("userId") userId: RequestBody,
        @Part("time") time: RequestBody,
        @Part("date") date: RequestBody,
        @Part image: MultipartBody.Part
    ): AttendanceResponse

    @POST("api/attendance/getByDate")
    suspend fun getAttendanceByUserIdAndDate(
        @Header("Authorization") token: String,
        @Body request: GetAttendanceByUserIdRequest
    ): AttendanceByDateResponse

    @GET("api/auth/getProfile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): ProfileResponse

    @PUT("api/auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): ChangePasswordResponse

    @GET("api/messages/{groupId}")
    suspend fun getMessages(@Path("groupId") groupId: String): MessageResponse

    @GET("api/groups/{userId}")
    suspend fun getGroupsByUserId(@Path("userId") userId: String): GroupsResponse

    @GET("api/attendance/{userId}")
    suspend fun getAttendanceByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): AttendanceByUserIdResponse

    @PUT("api/auth/update")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body request: UpdateUserRequest
    ): RegisterResponse

    @POST("api/auth/check-password")
    suspend fun checkPassword(
        @Header("Authorization") token: String,
        @Body request: CheckPasswordRequest
    ): CheckPasswordResponse

    @GET("api/working-days")
    suspend fun getWorkingDayByUserIdAndMonth(
        @Header("Authorization") token: String,
        @Query("userId") userId: String,
        @Query("month") month: String,
        @Query("year") year: String,
    ): WorkingDayByMonthResponse

    @GET("api/working-days/{workingDayId}")
    suspend fun getDetailWorkingDay(
        @Header("Authorization") token: String,
        @Path("workingDayId") workingDayId : String
    ) : DetailWorkingDayResponse

    @GET("api/auth/staff/{idDepartment}")
    suspend fun getListUserByDepartmentID(
        @Header("Authorization") token: String,
        @Path("idDepartment") idDepartment : String
    ) : StaffsResponse

    @GET("api/auth/search")
    suspend fun searchUser(
        @Header("Authorization") token: String,
        @Query("name") name : String
    ) : StaffsResponse

    @PUT("api/auth/accept")
    suspend fun acceptUser(
        @Header("Authorization") token: String,
        @Body request: AcceptUserRequest,
    ): AcceptUserResponse
}
