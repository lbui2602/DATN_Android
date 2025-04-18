package com.example.datn.socket

import io.socket.client.Socket
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor(val socket: Socket) {

    fun joinGroup(groupId: String) {
        socket.emit("join_group", groupId)
    }

    fun sendMessage(groupId: String, senderId: String, message: String) {
        val json = JSONObject().apply {
            put("groupId", groupId)
            put("senderId", senderId)
            put("message", message)
        }
        socket.emit("send_message", json)
    }
    fun connect(userId: String) {
        socket.connect()
        socket.on(Socket.EVENT_CONNECT) {
            socket.emit("user_connected", userId)
        }
    }

    fun disconnect() {
        socket.disconnect()
    }
}
