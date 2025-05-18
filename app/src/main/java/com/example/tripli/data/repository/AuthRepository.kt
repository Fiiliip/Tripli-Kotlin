package com.example.tripli.data.repository

import com.example.tripli.R
import com.example.tripli.data.model.User
import com.example.tripli.data.model.auth.LoginRequest
import com.example.tripli.data.model.auth.RegisterRequest
import com.example.tripli.data.network.api.AuthApiService
import com.example.tripli.di.StringProvider
import com.example.tripli.domain.AuthStore
import com.google.gson.Gson
import com.google.gson.JsonParser

class AuthRepository(
    private val authApiService: AuthApiService,
    private val authStore: AuthStore
) {
    private val gson = Gson()

    suspend fun register(credentials: RegisterRequest) {
        val response = authApiService.register(credentials)

        if (response.isSuccessful) {
            val data = response.body()?.getAsJsonObject("data") ?: throw Exception(StringProvider.get(R.string.error_malformed_response))

            val user = gson.fromJson(data.getAsJsonObject("user"), User::class.java)
            val token = data.get("token").asString

            authStore.setUser(user)
            authStore.setToken(token)

            return
        }

        val errorBody = response.errorBody()?.string()
        val errorMessage = errorBody?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString} ?: StringProvider.get(R.string.error_registration_failed)

        throw Exception(errorMessage)
    }

    suspend fun login(credentials: LoginRequest) {
        val response = authApiService.login(credentials)

        if (response.isSuccessful) {
            val data = response.body()?.getAsJsonObject("data") ?: throw Exception(StringProvider.get(R.string.error_malformed_response))

            val user = gson.fromJson(data.getAsJsonObject("user"), User::class.java)
            val token = data.get("token").asString

            authStore.setUser(user)
            authStore.setToken(token)

            return
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = errorJson?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString } ?: StringProvider.get(R.string.error_login_failed)

        throw Exception(errorMessage)
    }

    suspend fun logout() {
        try {
            authApiService.logout()
        } finally {
            authStore.setUser(null)
            authStore.setToken(null)
        }
    }

    suspend fun refreshToken(): Boolean {
        return try {
            val response = authApiService.refreshToken()
            if (response.isSuccessful) {
                val data = response.body()?.getAsJsonObject("data") ?: return false
                val token = data.get("token").asString
                authStore.setToken(token)
            }

            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateUserInfo(data: Map<String, String>) {
        var response = authApiService.updateUserInfo(data)

        if (response.code() == 401) {
            if (refreshToken()) {
                response = authApiService.updateUserInfo(data)
            } else {
                throw Exception(StringProvider.get(R.string.error_session_expired))
            }
        }

        if (response.isSuccessful) {
            val responseData = response.body()?.getAsJsonObject("data") ?: throw Exception(StringProvider.get(R.string.error_malformed_response))
            val user = gson.fromJson(responseData.getAsJsonObject("user"), User::class.java)
            authStore.setUser(user)

            return
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = errorJson?.let { JsonParser.parseString(it).asJsonObject["error"]?.asString} ?: StringProvider.get(R.string.error_info_update_failed)

        throw Exception(errorMessage)
    }
}