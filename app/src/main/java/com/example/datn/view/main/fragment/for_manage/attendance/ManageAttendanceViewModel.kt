package com.example.datn.view.main.fragment.for_manage.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.attendance.AttendanceRequest
import com.example.datn.models.attendance.GetAllAttendanceResponse
import com.example.datn.models.department.DepartmentResponse
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.message.MessageResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.socket.SocketManager
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAttendanceViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _departmentsResponse = MutableLiveData<DepartmentsResponses?>()
    val departmentsResponse: LiveData<DepartmentsResponses?> get() = _departmentsResponse

    private val _departmentResponse = MutableLiveData<DepartmentResponse?>()
    val departmentResponse: LiveData<DepartmentResponse?> get() = _departmentResponse

    private val _getAllAttendanceResponse = MutableLiveData<GetAllAttendanceResponse?>()
    val getAllAttendanceResponse: LiveData<GetAllAttendanceResponse?> get() = _getAllAttendanceResponse

    fun getAllAttendance(token: String, request: AttendanceRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getAllAttendance(token,request)
                if(response!=null){
                    _getAllAttendanceResponse.postValue(response)
                }
                else{
                    _getAllAttendanceResponse.postValue(null)
                }

            } catch (e: Exception) {
                _getAllAttendanceResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
    fun getDepartments(){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getDepartments()
                if(response!=null){
                    _departmentsResponse.postValue(response)
                }

            } catch (e: Exception) {
                print(e.toString())
                _departmentsResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun getDepartmentById(token : String,id: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getDepartmentById(token, id)
                if(response != null){
                    _departmentResponse.postValue(response)
                }
            } catch (e: Exception) {
                _departmentResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}