package com.example.datn.view.auth.fragment.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.login.LoginResponse
import com.example.datn.models.register.RegisterRequest
import com.example.datn.models.register.RegisterResponse
import com.example.datn.remote.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: LiveData<RegisterResponse?> get() = _registerResponse

    fun register(registerRequest: RegisterRequest){
        viewModelScope.launch {
            try {
                val response = repository.register(registerRequest)
                if(response!=null){
                    Log.e("loginResponse",response.toString())
                    _registerResponse.postValue(response)
                }

            } catch (e: Exception) {
                print(e.toString())
                _registerResponse.postValue(null)
            }
        }
    }
}
