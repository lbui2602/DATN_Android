package com.example.datn.view.main.fragment.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.attendance.AttendanceResponse
import com.example.datn.models.register.RegisterResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _profileResponse = MutableLiveData<RegisterResponse?>()
    val profileResponse: LiveData<RegisterResponse?> get() = _profileResponse

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun getProfile(token: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getProfile(token)
                if(response!=null  && response.code.toInt() ==1){
                    _profileResponse.postValue(response)
                }
                else{
                    _profileResponse.postValue(null)
                }

            } catch (e: Exception) {
                _profileResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}