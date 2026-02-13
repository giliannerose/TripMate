package com.example.tripmate.data.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }

    fun getUserName(): String {
        return prefs.getString("user_name", "User") ?: "User"
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}