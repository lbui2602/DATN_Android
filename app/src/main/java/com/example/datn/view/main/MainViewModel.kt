package com.example.datn.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.group.GroupsResponse
import com.example.datn.models.group.PrivateGroupResponse
import com.example.datn.models.message.Message
import com.example.datn.remote.repository.Repository
import com.example.datn.socket.SocketManager
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val socketManager: SocketManager
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> get() = _isLoading

    private val _message = MutableLiveData<Message?>()
    val message: LiveData<Message?> get() = _message

    private val _groupsResponse = MutableLiveData<GroupsResponse?>()
    val groupsResponse: LiveData<GroupsResponse?> get() = _groupsResponse

    init {
        listenForIncomingMessages()
    }
    suspend fun getGroupById(token: String, groupId: String): PrivateGroupResponse? {
        return try {
            _isLoading.postValue(true)
            val response = repository.getGroupById(token, groupId)
            response
        } catch (e: Exception) {
            null
        } finally {
            _isLoading.postValue(false)
        }
    }

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
    fun clearMessage(){
        viewModelScope.launch {
            _message.postValue(null)
        }
    }

    fun joinChatGroup(groupId: String) {
        socketManager.joinGroup(groupId)
    }

    fun listenForIncomingMessages() {
        socketManager.socket.on("receive_message") { args ->
            val messageJson = args[0] as JSONObject
            Log.e("Socket", messageJson.toString())

            val newMessage = Message(
                messageJson.getString("_id"),
                messageJson.getString("groupId"),
                messageJson.getString("message"),
                messageJson.getString("senderId"),
                messageJson.getString("senderImage"),
                messageJson.getString("senderName"),
                messageJson.getString("createdAt"),
                messageJson.getString("updatedAt")
            )
            viewModelScope.launch {
                _message.postValue(newMessage)
            }
        }
    }
    fun connect(){
        socketManager.connect(sharedPreferencesManager.getUserId().toString())
    }
    fun disconnect(){
        socketManager.disconnect()
    }
    fun clear(){
        socketManager.socket.off("receive_message")
    }
}