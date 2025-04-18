package com.example.datn.view.main.fragment.mess.dialog_list_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.group.GroupsResponse
import com.example.datn.models.group.PrivateGroupResponse
import com.example.datn.models.user.UserResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogUserViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _userResponse = MutableLiveData<UserResponse?>()
    val userResponse: LiveData<UserResponse?> get() = _userResponse

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
    suspend fun getPrivateGroup(token: String, userId2: String): PrivateGroupResponse? {
        return try {
            _isLoading.postValue(true)
            val response = repository.getPrivateGroup(token, userId2)
            response
        } catch (e: Exception) {
            null
        } finally {
            _isLoading.postValue(false)
        }
    }
}