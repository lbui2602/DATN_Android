package com.example.datn.view.auth.fragment.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.login.LoginRequest
import com.example.datn.models.login.LoginResponse
import com.example.datn.remote.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _isVisible = MutableLiveData<Boolean>()
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

    fun login(loginRequest: LoginRequest){
        Log.e("login",loginRequest.toString())
        viewModelScope.launch {
            try {
                val response = repository.login(loginRequest)
                if(response!=null){
                    Log.e("loginResponse",response.toString())
                    _loginResponse.postValue(response)
                }

            } catch (e: Exception) {
                print(e.toString())
                _loginResponse.postValue(null)
            }
        }
    }
}