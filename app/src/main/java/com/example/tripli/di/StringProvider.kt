package com.example.tripli.di

import android.content.Context
import androidx.annotation.StringRes

object StringProvider {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun get(@StringRes resId: Int): String {
        return appContext.getString(resId)
    }
}