package com.example.datn.view.auth.fragment.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.department.DepartmentsResponses
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.register.RegisterResponse
import com.example.datn.models.role.RolesResponse
import com.example.datn.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: LiveData<RegisterResponse?> get() = _registerResponse

    private val _rolesResponse = MutableLiveData<RolesResponse?>()
    val rolesResponse: LiveData<RolesResponse?> get() = _rolesResponse

    private val _departmentsResponse = MutableLiveData<DepartmentsResponses?>()
    val departmentsResponse: LiveData<DepartmentsResponses?> get() = _departmentsResponse

    private val _isVisible = MutableLiveData<Boolean?>()
    val isVisible: LiveData<Boolean?> get() = _isVisible

    private val _isVisibleConfirm = MutableLiveData<Boolean?>()
    val isVisibleConfirm: LiveData<Boolean?> get() = _isVisibleConfirm

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    init {
        _isVisible.value = false
        _isVisibleConfirm.value = false
    }

    fun changVisiblePassword() {
        if (_isVisible.value == true) {
            _isVisible.value = false
        } else {
            _isVisible.value = true
        }
    }

    fun changVisiblePasswordConfirm() {
        if (_isVisibleConfirm.value == true) {
            _isVisibleConfirm.value = false
        } else {
            _isVisibleConfirm.value = true
        }
    }

    fun register(registerRequest: RegisterRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.register(registerRequest)
                if(response != null){
                    _registerResponse.postValue(response)
                }

            } catch (e: Exception) {
                _registerResponse.postValue(null)
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
