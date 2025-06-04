package com.example.datn.remote.service

import com.example.datn.models.attendance.AttendanceByDateResponse
import com.example.datn.models.attendance.AttendanceByUserIdResponse
import com.example.datn.models.attendance.AttendanceRequest
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.models.attendance.GetAllAttendanceResponse
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.models.department.CreateDepartmentRequest
import com.example.datn.models.department.DepartmentResponse
import com.example.datn.models.upload_avatar.UploadAvatarResponse
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.department.UpdateDepartmentRequest
import com.example.datn.models.face_token.FaceTokenRequest
import com.example.datn.models.face_token.FaceTokenResponse
import com.example.datn.models.group.AddRequest
import com.example.datn.models.group.CreateGroupResponse
import com.example.datn.models.group.CreateRequest
import com.example.datn.models.group.GroupsResponse
import com.example.datn.models.group.LeaveRequest
import com.example.datn.models.group.PrivateGroupResponse
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.mail.SendOTPRequest
import com.example.datn.models.mail.VerifyOTPRequest
import com.example.datn.models.message.MessageResponse
import com.example.datn.models.password.ChangePasswordRequest
import com.example.datn.models.password.ChangePasswordResponse
import com.example.datn.models.password.CheckPasswordRequest
import com.example.datn.models.password.CheckPasswordResponse
import com.example.datn.models.password.ResetPasswordRequest
import com.example.datn.models.password.UpdatePasswordRequest
import com.example.datn.models.profile.ProfileResponse
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.role.RolesResponse
import com.example.datn.models.staff.AcceptUserRequest
import com.example.datn.models.staff.AcceptUserResponse
import com.example.datn.models.staff.StaffsResponse
import com.example.datn.models.training.TrainingResponse
import com.example.datn.models.update_user.UpdateUserRequest
import com.example.datn.models.user.UserResponse
import com.example.datn.models.user_info.UserInfoResponse
import com.example.datn.models.working_day.DetailWorkingDayResponse
import com.example.datn.models.working_day.WorkingDayByMonthResponse
import com.example.datn.models.working_day.WorkingDayForManageResponse
import com.example.datn.view.main.fragment.working_day.DetailWorkingDayViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    //system
    @GET("api/roles")
    suspend fun getRoles(): RolesResponse

    @GET("api/departments")
    suspend fun getDepartments(): DepartmentsResponses

    @Multipart
    @POST("/api/face/verify-face")
    suspend fun attendance(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody,
        @Part("time") time: RequestBody,
        @Part("date") date: RequestBody
    ): AttendanceResponse

    @Multipart
    @POST("/api/face/compare-faces")
    suspend fun compare(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody,
        @Part("time") time: RequestBody,
        @Part("date") date: RequestBody
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

    @GET("api/auth/getUserInfo")
    suspend fun getUserInfo(
        @Header("Authorization") token: String,
    ): UserInfoResponse

    @PUT("api/auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): ChangePasswordResponse

    @PUT("api/auth/reset-password")
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body request: ResetPasswordRequest
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
        @Path("idDepartment") idDepartment : String,
        @Query("search") search : String?
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

    @Multipart
    @POST("/api/training/upload-file")
    suspend fun uploadAvatar(
        @Part("name") name: RequestBody,
        @Part file: MultipartBody.Part
    ): TrainingResponse

    // face-service
    @Multipart
    @POST("/api/training")
    suspend fun training(
        @Part("name") name: RequestBody,
        @Part file: MultipartBody.Part
    ): TrainingResponse

    @GET("api/auth/getAll/{userId}")
    suspend fun getAllUser(
        @Header("Authorization") token: String,
        @Path("userId") userId : String
    ) : UserResponse

    @GET("api/groups/getPrivateGroup/{userId2}")
    suspend fun getPrivateGroup(
        @Header("Authorization") token: String,
        @Path("userId2") userId2 : String
    ) : PrivateGroupResponse

    @GET("api/groups/getById/{groupId}")
    suspend fun getGroupById(
        @Header("Authorization") token: String,
        @Path("groupId") groupId : String
    ) : PrivateGroupResponse

    @GET("api/departments/{id}")
    suspend fun getDepartmentById(
        @Header("Authorization") token: String,
        @Path("id") id : String
    ) : DepartmentResponse

    @GET("api/auth/getProfileByUserId/{userId")
    suspend fun getProfileByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId : String
    ) : ProfileResponse

    @POST("api/attendance/getAll")
    suspend fun getAllAttendance(
        @Header("Authorization") token: String,
        @Body request: AttendanceRequest
    ) : GetAllAttendanceResponse

    @POST("api/working-days/getAll")
    suspend fun getAllWorkingDay(
        @Header("Authorization") token: String,
        @Body request: AttendanceRequest
    ) : WorkingDayForManageResponse

    @POST("api/groups")
    suspend fun createGroup(
        @Header("Authorization") token: String,
        @Body request: CreateRequest
    ) : CreateGroupResponse

    @POST("api/groups/delete")
    suspend fun leaveGroup(
        @Header("Authorization") token: String,
        @Body request : LeaveRequest
    ) : TrainingResponse

    @POST("api/groups/add")
    suspend fun joinGroup(
        @Header("Authorization") token: String,
        @Body request : AddRequest
    ) : TrainingResponse

    @POST("api/groups/getUserInGroup/{groupId}")
    suspend fun getUserInGroup(
        @Header("Authorization") token: String,
        @Path("groupId") groupId : String
    ) : UserResponse

    @PUT("api/departments/{id}")
    suspend fun updateDepartment(
        @Header("Authorization") token: String,
        @Path("id") id : String,
        @Body request : UpdateDepartmentRequest
    ) : DepartmentResponse

    @DELETE("api/departments/{id}")
    suspend fun deleteDepartment(
        @Header("Authorization") token: String,
        @Path("id") id : String,
    ) : TrainingResponse

    @POST("api/departments")
    suspend fun createDepartment(
        @Header("Authorization") token: String,
        @Body request : CreateDepartmentRequest
    ) : DepartmentResponse

    @POST("api/mail/send-otp")
    suspend fun sendOtp(
        @Body request : SendOTPRequest
    ) : TrainingResponse

    @POST("api/mail/verify-otp")
    suspend fun verifyOtp(
        @Body request : VerifyOTPRequest
    ) : TrainingResponse

    @PUT("api/auth/update-password")
    suspend fun updatePassword(
        @Body request : UpdatePasswordRequest
    ) : TrainingResponse
}
