package com.example.tripli.data.network

import com.example.tripli.data.network.api.AuthApiService
import com.example.tripli.domain.AuthStore
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthGuard(
    private val authApiService: AuthApiService,
    private val authStore: AuthStore,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.priorResponse != null) {
            return null // Už raz prebehol pokus o obnovenie tokenu.
        }

        val refreshTokenCall = authApiService.refreshTokenCall()
        val refreshTokenResponse = try {
            refreshTokenCall.execute()
        } catch (e: Exception) {
            return null
        }

        if (refreshTokenResponse.isSuccessful) {
            val data = refreshTokenResponse.body()?.getAsJsonObject("data") ?: return null
            val newToken = data.get("token").asString

            authStore.setToken(newToken)

            return response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }

        return null
    }
}