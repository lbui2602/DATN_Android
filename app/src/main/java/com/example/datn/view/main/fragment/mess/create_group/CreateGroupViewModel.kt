package com.example.datn.view.main.fragment.mess.create_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.group.CreateGroupResponse
import com.example.datn.models.group.CreateRequest
import com.example.datn.models.group.PrivateGroupResponse
import com.example.datn.models.user.UserResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _userResponse = MutableLiveData<UserResponse?>()
    val userResponse: LiveData<UserResponse?> get() = _userResponse

    private val _createGroupResponse = MutableLiveData<CreateGroupResponse?>()
    val createGroupResponse: LiveData<CreateGroupResponse?> get() = _createGroupResponse

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

    fun createGroup(token : String,request: CreateRequest){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.createGroup(token,request)
                if(response!=null){
                    _createGroupResponse.postValue(response)
                }
                else{
                    _createGroupResponse.postValue(null)
                }

            } catch (e: Exception) {
                _createGroupResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}