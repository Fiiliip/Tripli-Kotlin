package com.example.tripli.domain

import com.example.tripli.data.model.User

class AuthStore {
    private var user: User? = null
    private var token: String? = null

    fun setUser(user: User?) {
        this.user = user
    }

    fun setToken(token: String?) {
        this.token = token
    }

    fun getUser(): User? {
        return user
    }

    fun getToken(): String? {
        return token
    }

    fun isLoggedIn(): Boolean {
        return user != null && token != null
    }
}