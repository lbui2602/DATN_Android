package com.example.datn.view.main.fragment.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.Message
import com.example.datn.remote.repository.Repository
import com.example.datn.socket.SocketManager
import com.example.datn.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val socketManager: SocketManager
) : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    init {
        listenForIncomingMessages()
    }

    fun getMessages(groupId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMessages(groupId)
                _messages.postValue(response ?: emptyList())
            } catch (e: Exception) {
                _messages.postValue(emptyList())
            }
        }
    }

    fun joinChatGroup(groupId: String) {
        socketManager.joinGroup(groupId)
    }

    fun sendMessage(groupId: String, senderId: String, message: String) {
        socketManager.sendMessage(groupId, senderId, message)
    }

    private fun listenForIncomingMessages() {
        socketManager.socket.on("receive_message") { args ->
            val messageJson = args[0] as JSONObject
            val newMessage = Message(
                messageJson.getString("_id"),
                messageJson.getString("groupId"),
                messageJson.getString("senderId"),
                messageJson.getString("message"),
                messageJson.getString("createdAt"),
                messageJson.getString("updatedAt")
            )

            viewModelScope.launch {
                val currentMessages = _messages.value?.toMutableList() ?: mutableListOf()
                currentMessages.add(newMessage)
                _messages.postValue(currentMessages)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        socketManager.socket.off("receive_message")
    }
}
