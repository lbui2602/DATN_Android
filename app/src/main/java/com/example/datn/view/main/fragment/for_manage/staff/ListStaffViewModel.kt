package com.example.datn.view.main.fragment.for_manage.staff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun getListUserByDepartmentID(token : String,idDepartment: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getListUserByDepartmentID(token, idDepartment)
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
}