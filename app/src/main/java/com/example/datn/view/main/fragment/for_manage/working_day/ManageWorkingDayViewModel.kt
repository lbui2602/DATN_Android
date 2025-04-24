package com.example.datn.view.main.fragment.for_manage.working_day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.attendance.AttendanceRequest
import com.example.datn.models.attendance.GetAllAttendanceResponse
import com.example.datn.models.department.DepartmentResponse
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.working_day.WorkingDayForManageResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageWorkingDayViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _departmentsResponse = MutableLiveData<DepartmentsResponses?>()
    val departmentsResponse: LiveData<DepartmentsResponses?> get() = _departmentsResponse

    private val _departmentResponse = MutableLiveData<DepartmentResponse?>()
    val departmentResponse: LiveData<DepartmentResponse?> get() = _departmentResponse

    private val _workingDayResponse = MutableLiveData<WorkingDayForManageResponse?>()
    val workingDayResponse: LiveData<WorkingDayForManageResponse?> get() = _workingDayResponse

    fun getAllWorkingDay(token: String, request: AttendanceRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getAllWorkingDay(token,request)
                if(response!=null){
                    _workingDayResponse.postValue(response)
                }
                else{
                    _workingDayResponse.postValue(null)
                }

            } catch (e: Exception) {
                _workingDayResponse.postValue(null)
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