package com.example.tripli.data.network

import com.example.tripli.domain.AuthStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authStore: AuthStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer ${authStore.getToken()}")
            .build()

        return chain.proceed(newRequest)
    }
}