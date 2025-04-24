package com.example.datn.view.main.fragment.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.models.user_info.UserInfoResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading


    private val _userInfoResponse = MutableLiveData<UserInfoResponse?>()
    val userInfoResponse: LiveData<UserInfoResponse?> get() = _userInfoResponse

    fun getUserInfo(token: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getUserInfo(token)
                if(response!=null){
                    _userInfoResponse.postValue(response)
                }
                else{
                    _userInfoResponse.postValue(null)
                }

            } catch (e: Exception) {
                _userInfoResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}