package com.example.datn.view.auth.fragment.update_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.mail.SendOTPRequest
import com.example.datn.models.password.UpdatePasswordRequest
import com.example.datn.models.training.TrainingResponse
import com.example.datn.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _isVisibleNew = MutableLiveData<Boolean?>()
    val isVisibleNew: LiveData<Boolean?> get() = _isVisibleNew

    private val _isVisibleConfirm = MutableLiveData<Boolean?>()
    val isVisibleConfirm: LiveData<Boolean?> get() = _isVisibleConfirm

    private val _updatePasswordResponse = MutableLiveData<TrainingResponse?>()
    val updatePasswordResponse: LiveData<TrainingResponse?> get() = _updatePasswordResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    init {
        _isVisibleNew.value = false
        _isVisibleConfirm.value = false
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

    fun updatePassword(updatePasswordRequest: UpdatePasswordRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.updatePassword(updatePasswordRequest)
                if(response!=null){
                    _updatePasswordResponse.postValue(response)
                }
            } catch (e: Exception) {
                _updatePasswordResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}