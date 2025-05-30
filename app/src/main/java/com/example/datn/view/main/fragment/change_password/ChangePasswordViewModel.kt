package com.example.datn.view.main.fragment.change_password

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.models.password.ChangePasswordRequest
import com.example.datn.models.password.ChangePasswordResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _changePasswordResponse = MutableLiveData<ChangePasswordResponse?>()
    val changePasswordResponse: LiveData<ChangePasswordResponse?> get() = _changePasswordResponse

    private val _isVisible = MutableLiveData<Boolean?>()
    val isVisible: LiveData<Boolean?> get() = _isVisible

    private val _isVisibleNew = MutableLiveData<Boolean?>()
    val isVisibleNew: LiveData<Boolean?> get() = _isVisibleNew

    private val _isVisibleConfirm = MutableLiveData<Boolean?>()
    val isVisibleConfirm: LiveData<Boolean?> get() = _isVisibleConfirm

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    init {
        _isVisible.value = false
        _isVisibleNew.value = false
        _isVisibleConfirm.value = false
    }

    fun changVisiblePassword() {
        if (_isVisible.value == true) {
            _isVisible.value = false
        } else {
            _isVisible.value = true
        }
    }

    fun changVisiblePasswordNew() {
        if (_isVisibleNew.value == true) {
            _isVisibleNew.value = false
        } else {
            _isVisibleNew.value = true
        }
    }

    fun changVisiblePasswordConfirm() {
        if (_isVisibleConfirm.value == true) {
            _isVisibleConfirm.value = false
        } else {
            _isVisibleConfirm.value = true
        }
    }

    fun changePassword(token: String,request: ChangePasswordRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.changePassword(token,request)
                if(response != null){
                    _changePasswordResponse.postValue(response)
                }
                else{
                    _changePasswordResponse.postValue(null)
                }

            } catch (e: Exception) {
                _changePasswordResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}