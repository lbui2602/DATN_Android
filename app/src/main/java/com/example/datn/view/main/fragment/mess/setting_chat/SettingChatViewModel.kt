package com.example.datn.view.main.fragment.mess.setting_chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.group.GroupsResponse
import com.example.datn.models.group.LeaveRequest
import com.example.datn.models.training.TrainingResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingChatViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _leaveGroupResponse = MutableLiveData<TrainingResponse?>()
    val leaveGroupResponse: LiveData<TrainingResponse?> get() = _leaveGroupResponse

    private val _joinGroupResponse = MutableLiveData<TrainingResponse?>()
    val joinGroupResponse: LiveData<TrainingResponse?> get() = _joinGroupResponse

    fun leaveGroup(token: String, request: LeaveRequest) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.leaveGroup(token,request)
                if (response != null) {
                    _leaveGroupResponse.postValue(response)
                } else {
                    _leaveGroupResponse.postValue(null)
                }

            } catch (e: Exception) {
                _leaveGroupResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun joinGroup(token: String, request: LeaveRequest) {
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
}