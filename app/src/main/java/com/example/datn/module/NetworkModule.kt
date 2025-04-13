package com.example.datn.module

import com.example.datn.remote.service.ApiService
import com.example.datn.remote.service.FaceApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //192.168.1.101
    //192.168.52.52
    //172.20.10.4
    private const val BASE_URL_1 = "http://172.20.10.4:3000/"  // Backend chính
    private const val BASE_URL_2 = "https://api-us.faceplusplus.com/facepp/v3/"  // Face++

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @Named("retrofit1")
    fun provideRetrofit1(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_1)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("retrofit2")
    fun provideRetrofit2(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMainApiService(@Named("retrofit1") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFaceApiService(@Named("retrofit2") retrofit: Retrofit): FaceApiService {
        return retrofit.create(FaceApiService::class.java)
    }
}
