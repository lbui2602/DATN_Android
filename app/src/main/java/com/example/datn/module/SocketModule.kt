package com.example.datn.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    private const val SERVER_URL = "http://127.0.0.1:3000"

    @Provides
    @Singleton
    fun provideSocket(): Socket {
        return try {
            val options = IO.Options().apply {
                reconnection = true
                reconnectionAttempts = Int.MAX_VALUE
                reconnectionDelay = 1000 // 1s delay giữa mỗi lần reconnect
            }
            IO.socket(SERVER_URL, options)
        } catch (e: Exception) {
            throw RuntimeException("Failed to connect socket", e)
        }
    }
}
