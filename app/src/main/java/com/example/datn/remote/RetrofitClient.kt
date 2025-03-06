package com.example.datn.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.52.52:3000/"
    private var instance: Retrofit? = null

    private fun getInstance(): Retrofit {
        if (instance == null) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }

    fun <T> create(service: Class<T>): T {
        return getInstance().create(service)
    }
}
