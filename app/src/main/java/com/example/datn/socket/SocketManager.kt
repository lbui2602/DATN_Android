package com.example.datn.socket

import io.socket.client.Socket
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor(val socket: Socket) {

    private var isUserConnected = false

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
        if (!socket.connected()) {
            socket.connect()

            socket.off(Socket.EVENT_CONNECT) // Gỡ listener cũ
            socket.on(Socket.EVENT_CONNECT) {
                socket.emit("user_connected", userId)
                isUserConnected = true
            }
        } else {
            socket.emit("user_connected", userId)
        }
    }



    fun disconnect() {
        socket.disconnect()
        isUserConnected = false
    }
    fun onBlockAccount(callback: (userId: String) -> Unit) {
        socket.off("block_account")
        socket.on("block_account") { args ->
            if (args.isNotEmpty()) {
                val data = args[0] as? JSONObject
                val userId = data?.optString("userId")
                userId?.let {
                    callback(it)
                }
            }
        }
    }
}
