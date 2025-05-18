package com.example.tripli.data.network.api

import com.example.tripli.data.model.auth.LoginRequest
import com.example.tripli.data.model.auth.RegisterRequest
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("v1/auth/signup")
    suspend fun register(@Body credentials: RegisterRequest): Response<JsonObject>

    @POST("v1/auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<JsonObject>

    @POST("v1/auth/invalidate")
    suspend fun logout()

    @POST("v1/auth/refresh")
    fun refreshTokenCall(): Call<JsonObject>

    @POST("v1/auth/refresh")
    suspend fun refreshToken(): Response<JsonObject>

    @POST("v1/auth/info")
    suspend fun updateUserInfo(@Body data: Map<String, String>): Response<JsonObject>
}
