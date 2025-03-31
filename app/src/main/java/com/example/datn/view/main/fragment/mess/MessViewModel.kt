package com.example.datn.view.main.fragment.mess

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.group.GroupsResponse
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MessViewModel @Inject constructor(
    private val repository: Repository,private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _groupsResponse = MutableLiveData<GroupsResponse?>()
    val groupsResponse: LiveData<GroupsResponse?> get() = _groupsResponse

    fun getGroupsByUserId(userId: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.getGroupByUserId(userId)
                if(response!=null  && response.code.toInt() ==1){
                    _groupsResponse.postValue(response)
                }
                else{
                    _groupsResponse.postValue(null)
                }

            } catch (e: Exception) {
                _groupsResponse.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }
}