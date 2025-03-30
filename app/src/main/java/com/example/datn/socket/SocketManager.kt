package com.example.datn.socket

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private const val SERVER_URL = "http://192.168.1.101:3000"
    val socket: Socket = IO.socket(SERVER_URL)

    fun connect() {
        socket.connect()
    }

    fun joinGroup(groupId: String) {
        socket.emit("join_group", groupId)
    }

    fun sendMessage(groupId: String, senderId: String, message: String) {
        val json = JSONObject()
        json.put("groupId", groupId)
        json.put("senderId", senderId)
        json.put("message", message)
        socket.emit("send_message", json)
    }
}
