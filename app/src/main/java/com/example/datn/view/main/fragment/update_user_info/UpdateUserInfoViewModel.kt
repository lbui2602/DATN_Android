package com.example.datn.view.main.fragment.update_user_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.password.ChangePasswordRequest
import com.example.datn.models.password.ChangePasswordResponse
import com.example.datn.models.password.CheckPasswordRequest
import com.example.datn.models.password.CheckPasswordResponse
import com.example.datn.models.password.ResetPasswordRequest
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.role.RolesResponse
import com.example.datn.models.update_user.UpdateUserRequest
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateUserInfoViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _updateUserResponse = MutableLiveData<RegisterResponse?>()
    val updateUserResponse: LiveData<RegisterResponse?> get() = _updateUserResponse

    private val _checkPasswordResponse = MutableLiveData<CheckPasswordResponse?>()
    val checkPasswordResponse: LiveData<CheckPasswordResponse?> get() = _checkPasswordResponse

    private val _rolesResponse = MutableLiveData<RolesResponse?>()
    val rolesResponse: LiveData<RolesResponse?> get() = _rolesResponse

    private val _departmentsResponse = MutableLiveData<DepartmentsResponses?>()
    val departmentsResponse: LiveData<DepartmentsResponses?> get() = _departmentsResponse

    private val _resetPasswordResponse = MutableLiveData<ChangePasswordResponse?>()
    val resetPasswordResponse: LiveData<ChangePasswordResponse?> get() = _resetPasswordResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _isVisible = MutableLiveData<Boolean?>()
    val isVisible: LiveData<Boolean?> get() = _isVisible

    init {
        _isVisible.value = false
    }

    fun changVisiblePassword() {
        if (_isVisible.value == true) {
            _isVisible.value = false
        } else {
            _isVisible.value = true
        }
    }

    fun resetPassword(token: String,request: ResetPasswordRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.resetPassword(token,request)
                if(response != null){
                    _resetPasswordResponse.postValue(response)
                }
                else{
                    _resetPasswordResponse.postValue(null)
                }

            } catch (e: Exception) {
                _resetPasswordResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun updateUser(token : String, request: UpdateUserRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.updateUser(token,request)
                if(response != null){
                    _updateUserResponse.postValue(response)
                }

            } catch (e: Exception) {
                _updateUserResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
    fun checkPassword(token : String, password: CheckPasswordRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.checkPassword(token,password)
                Log.e("check vm",response.toString())
                if(response != null){
                    _checkPasswordResponse.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("check vm",e.toString())
                _checkPasswordResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun getRoles(){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getRoles()
                if(response!=null){
                    _rolesResponse.postValue(response)
                }

            } catch (e: Exception) {
                print(e.toString())
                _rolesResponse.postValue(null)
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
}