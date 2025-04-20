package com.example.datn.view.main.fragment.for_manage.staff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.department.DepartmentResponse
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.staff.AcceptUserRequest
import com.example.datn.models.staff.AcceptUserResponse
import com.example.datn.models.staff.StaffsResponse
import com.example.datn.models.working_day.IdDepartment
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListStaffViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _staffsResponse = MutableLiveData<StaffsResponse?>()
    val staffsResponse: LiveData<StaffsResponse?> get() = _staffsResponse

    private val _acceptUserResponse = MutableLiveData<AcceptUserResponse?>()
    val acceptUserResponse: LiveData<AcceptUserResponse?> get() = _acceptUserResponse

    private val _departmentResponse = MutableLiveData<DepartmentResponse?>()
    val departmentResponse: LiveData<DepartmentResponse?> get() = _departmentResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun getListUserByDepartmentID(token : String,idDepartment: String,search : String?){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getListUserByDepartmentID(token, idDepartment,search)
                if(response != null){
                    _staffsResponse.postValue(response)
                }
            } catch (e: Exception) {
                _staffsResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun acceptUser(token : String,request: AcceptUserRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.acceptUser(token, request)
                if(response != null){
                    _acceptUserResponse.postValue(response)
                }
            } catch (e: Exception) {
                _acceptUserResponse.postValue(null)
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

    fun searchUser(token : String,name: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.searchUser(token, name)
                if(response != null){
                    _staffsResponse.postValue(response)
                }
            } catch (e: Exception) {
                _staffsResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}