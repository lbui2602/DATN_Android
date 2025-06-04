package com.example.datn.view.auth.fragment.send_otp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.mail.SendOTPRequest
import com.example.datn.models.training.TrainingResponse
import com.example.datn.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendOtpViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _sendOtpResponse = MutableLiveData<TrainingResponse?>()
    val sendOtpResponse: LiveData<TrainingResponse?> get() = _sendOtpResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun sendOtp(sendOTPRequest: SendOTPRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.sendOtp(sendOTPRequest)
                if(response!=null){
                    _sendOtpResponse.postValue(response)
                }
            } catch (e: Exception) {
                _sendOtpResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}