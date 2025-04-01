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

    private const val SERVER_URL = "http://192.168.52.52:3000"

    @Provides
    @Singleton
    fun provideSocket(): Socket {
        return IO.socket(SERVER_URL).apply { connect() }
    }
}