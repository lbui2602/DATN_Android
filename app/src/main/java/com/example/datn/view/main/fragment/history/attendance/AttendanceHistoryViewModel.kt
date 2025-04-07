package com.example.datn.view.main.fragment.history.attendance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.attendance.AttendanceByDateResponse
import com.example.datn.models.attendance.AttendanceByUserIdResponse
import com.example.datn.models.attendance.GetAttendanceByUserIdRequest
import com.example.datn.models.message.MessageResponse
import com.example.datn.models.password.ChangePasswordRequest
import com.example.datn.remote.repository.Repository
import com.example.datn.socket.SocketManager
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceHistoryViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

    private val _attendanceResponse = MutableLiveData<AttendanceByUserIdResponse?>()
    val attendanceResponse: LiveData<AttendanceByUserIdResponse?> get() = _attendanceResponse

    private val _attendanceByDateResponse = MutableLiveData<AttendanceByDateResponse?>()
    val attendanceByDateResponse: LiveData<AttendanceByDateResponse?> get() = _attendanceByDateResponse

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun getAttendanceByUserId(token: String,userId: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getAttendanceByUserId(token,userId)
                if(response!=null  && response.code.toInt() ==1){
                    _attendanceResponse.postValue(response)
                }
                else{
                    _attendanceResponse.postValue(null)
                }

            } catch (e: Exception) {
                _attendanceResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
    fun getAttendanceByUserIdAndDate(token:String,request : GetAttendanceByUserIdRequest){
        Log.e("getAttendanceByUserIdAndDate","hi")
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getAttendanceByUserIdAndDate(token, request)
                if(response!=null){
                    _attendanceByDateResponse.postValue(response)
                }
                else{
                    _attendanceByDateResponse.postValue(null)
                }

            } catch (e: Exception) {
                Log.e("getAttendanceByUserIdAndDate",e.toString())
                _attendanceByDateResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}