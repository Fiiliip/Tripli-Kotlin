package com.example.tripli.di

import com.example.tripli.data.network.AuthGuard
import com.example.tripli.data.network.AuthInterceptor
import com.example.tripli.data.network.api.AuthApiService
import com.example.tripli.data.network.api.ItineraryApiService
import com.example.tripli.data.repository.AuthRepository
import com.example.tripli.data.repository.ItineraryRepository
import com.example.tripli.domain.AuthStore
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer {
    private val BASE_URL = "http://192.168.0.105:8000/api/"

    val authStore: AuthStore by lazy {
        AuthStore()
    }

    private val okHttpClientWithoutGuard: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(authStore))
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val retrofitWithoutInterceptor: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClientWithoutGuard)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApiService: AuthApiService by lazy {
        retrofitWithoutInterceptor.create(AuthApiService::class.java)
    }

    private val okHttpClientWithAuth: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(authStore))
        .authenticator(AuthGuard(authApiService, authStore))
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val retrofitWithAuth: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClientWithAuth)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val itineraryApiService: ItineraryApiService by lazy {
        retrofitWithAuth.create(ItineraryApiService::class.java)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authApiService, authStore)
    }

    val itineraryRepository: ItineraryRepository by lazy {
        ItineraryRepository(itineraryApiService)
    }
}