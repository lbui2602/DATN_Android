package com.example.datn.view.main.fragment.mess.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn.models.message.Message
import com.example.datn.models.message.MessageResponse
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

    private val _messages = MutableLiveData<MessageResponse?>()
    val messages: LiveData<MessageResponse?> get() = _messages

    init {
        listenForIncomingMessages()
    }

    fun getMessages(groupId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMessages(groupId)
                _messages.value = response
            } catch (e: Exception) {
                _messages.value = null
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
            Log.e("Socket 2", messageJson.toString())

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
                val currentMessagesResponse = _messages.value
                val updatedMessages = currentMessagesResponse?.messages?.toMutableList() ?: mutableListOf()
                updatedMessages.add(newMessage)

                _messages.value = MessageResponse(
                    code = "1", // Ép thành Thành công luôn
                    message = "Thành công",
                    messages = updatedMessages
                )

                Log.e("Message Updated", _messages.value.toString())
            }

        }
    }
}

