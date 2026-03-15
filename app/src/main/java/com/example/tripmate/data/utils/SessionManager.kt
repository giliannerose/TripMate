package com.example.tripmate.data.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserSession(userId: Int, name: String) {
        prefs.edit()
            .putInt("USER_ID", userId)
            .putString("user_name", name)
            .apply()
    }

    fun getUserId(): Int {
        return prefs.getInt("USER_ID", NO_USER)
    }

    fun getUserName(): String {
        return prefs.getString("user_name", "User") ?: "User"
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val NO_USER = -1
    }
}