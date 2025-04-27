package com.example.datn.remote.repository

import android.util.Log
import com.example.datn.models.attendance.AttendanceByDateResponse
import com.example.datn.models.attendance.AttendanceByUserIdResponse
import com.example.datn.models.attendance.AttendanceRequest
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.models.attendance.GetAllAttendanceResponse
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.models.department.DepartmentResponse
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.face_api.AddFaceResponse
import com.example.datn.models.face_api.CompareFaceResponse
import com.example.datn.models.face_api.CreateFaceSetResponse
import com.example.datn.models.face_api.DetectFaceResponse
import com.example.datn.models.face_token.FaceTokenRequest
import com.example.datn.models.face_token.FaceTokenResponse
import com.example.datn.models.group.CreateGroupResponse
import com.example.datn.models.group.CreateRequest
import com.example.datn.models.group.GroupsResponse
import com.example.datn.models.group.LeaveRequest
import com.example.datn.models.group.PrivateGroupResponse
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
import com.example.datn.models.staff.AcceptUserRequest
import com.example.datn.models.staff.AcceptUserResponse
import com.example.datn.models.staff.StaffsResponse
import com.example.datn.models.training.TrainingResponse
import com.example.datn.models.update_user.UpdateUserRequest
import com.example.datn.models.upload_avatar.UploadAvatarResponse
import com.example.datn.models.user.UserResponse
import com.example.datn.models.user_info.UserInfoResponse
import com.example.datn.models.working_day.DetailWorkingDayResponse
import com.example.datn.models.working_day.WorkingDay
import com.example.datn.models.working_day.WorkingDayByMonthResponse
import com.example.datn.models.working_day.WorkingDayForManageResponse
import com.example.datn.remote.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService
) {

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

    suspend fun uploadAvatar(name: RequestBody, image: MultipartBody.Part): TrainingResponse {
        return apiService.uploadAvatar( name,image)
    }
    //face-service-python
    suspend fun training(name: RequestBody, image: MultipartBody.Part): TrainingResponse {
        return apiService.training( name,image)
    }

    suspend fun attendance(
        token: String,
        file: MultipartBody.Part,
        userId: RequestBody,
        time: RequestBody,
        date: RequestBody,
    ): AttendanceResponse {
        return apiService.attendance(token, file,userId, time, date)
    }

    suspend fun compare(
        token: String,
        file: MultipartBody.Part,
        fileName: RequestBody,
        userId: RequestBody,
        time: RequestBody,
        date: RequestBody,
    ): AttendanceResponse {
        return apiService.compare(token, file,fileName,userId, time, date)
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

    suspend fun getUserInfo(token: String): UserInfoResponse {
        return apiService.getUserInfo(token)
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
        token: String,
        workingDayId: String
    ): DetailWorkingDayResponse {
        return apiService.getDetailWorkingDay(token, workingDayId)
    }

    suspend fun getListUserByDepartmentID(
        token: String,
        idDepartment: String,
        search:String?
    ) : StaffsResponse {
        return apiService.getListUserByDepartmentID(token,idDepartment,search)
    }

    suspend fun searchUser(
        token: String,
        name: String
    ) : StaffsResponse {
        return apiService.searchUser(token,name)
    }

    suspend fun acceptUser(
        token: String,
        request : AcceptUserRequest
    ) : AcceptUserResponse {
        return apiService.acceptUser(token,request)
    }

    suspend fun getAllUser(
        token: String,
        userId: String
    ) : UserResponse {
        return apiService.getAllUser(token,userId)
    }
    suspend fun getPrivateGroup(
        token: String,
        userId2: String
    ) : PrivateGroupResponse {
        return apiService.getPrivateGroup(token,userId2)
    }

    suspend fun getGroupById(
        token: String,
        groupId: String
    ) : PrivateGroupResponse {
        return apiService.getGroupById(token,groupId)
    }
    suspend fun getDepartmentById(
        token: String,
        id: String
    ) : DepartmentResponse {
        return apiService.getDepartmentById(token,id)
    }

    suspend fun getProfileByUserId(
        token : String,
        userId : String
    ) : ProfileResponse {
        return apiService.getProfileByUserId(token,userId)
    }

    suspend fun getAllAttendance(
        token : String,
        request: AttendanceRequest
    ) : GetAllAttendanceResponse {
        return apiService.getAllAttendance(token,request)
    }
    suspend fun getAllWorkingDay(
        token : String,
        request: AttendanceRequest
    ) : WorkingDayForManageResponse {
        return apiService.getAllWorkingDay(token,request)
    }

    suspend fun createGroup(
        token : String,
        request: CreateRequest
    ) : CreateGroupResponse {
        return apiService.createGroup(token,request)
    }

    suspend fun leaveGroup(
        token: String,
        request: LeaveRequest
    ) : TrainingResponse {
        return apiService.leaveGroup(token,request)
    }

    suspend fun joinGroup(
        token: String,
        request: LeaveRequest
    ) : TrainingResponse {
        return apiService.joinGroup(token,request)
    }
}