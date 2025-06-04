package com.example.datn.view.auth.fragment.verify_otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.mail.VerifyOTPRequest
import com.example.datn.models.training.TrainingResponse
import com.example.datn.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _verifyOtpResponse = MutableLiveData<TrainingResponse?>()
    val verifyOtpResponse: LiveData<TrainingResponse?> get() = _verifyOtpResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    fun verifyOtp(verifyOTPRequest: VerifyOTPRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.verifyOtp(verifyOTPRequest)
                if(response!=null){
                    _verifyOtpResponse.postValue(response)
                }
            } catch (e: Exception) {
                _verifyOtpResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}