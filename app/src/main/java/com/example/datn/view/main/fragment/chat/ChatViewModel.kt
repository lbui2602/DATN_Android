package com.example.datn.view.main.fragment.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.Message
import com.example.datn.remote.repository.Repository
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: Repository, private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val _messages = MutableLiveData<List<Message>?>()
    val messages: LiveData<List<Message>?> get() = _messages

    fun getMessages(groupId : String){
        viewModelScope.launch {
            try {
                val response = repository.getMessages(groupId)
                if(response!=null){
                    _messages.postValue(response)
                }
                else{
                    _messages.postValue(null)
                }

            } catch (e: Exception) {
                _messages.postValue(null)
            }
        }
    }
}