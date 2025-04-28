package com.example.datn.view.main.fragment.mess.add_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.group.AddRequest
import com.example.datn.models.group.LeaveRequest
import com.example.datn.models.training.TrainingResponse
import com.example.datn.models.user.UserResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _joinGroupResponse = MutableLiveData<TrainingResponse?>()
    val joinGroupResponse: LiveData<TrainingResponse?> get() = _joinGroupResponse

    private val _userResponse = MutableLiveData<UserResponse?>()
    val userResponse: LiveData<UserResponse?> get() = _userResponse

    fun joinGroup(token: String, request: AddRequest) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.joinGroup(token,request)
                if (response != null) {
                    _joinGroupResponse.postValue(response)
                } else {
                    _joinGroupResponse.postValue(null)
                }

            } catch (e: Exception) {
                _joinGroupResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun getAllUser(token : String,userId: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getAllUser(token,userId)
                if(response!=null){
                    _userResponse.postValue(response)
                }
                else{
                    _userResponse.postValue(null)
                }

            } catch (e: Exception) {
                _userResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}