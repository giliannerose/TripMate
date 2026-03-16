package com.example.tripmate.data.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("TripMatePrefs", Context.MODE_PRIVATE)

    fun saveUserSession(userId: String, userName: String, userEmail: String) {
        prefs.edit {
            putString("userId", userId)
            putString("user_name", userName)
            putString("user_email", userEmail)
        }
    }

    fun getUserId(): String? {
        return prefs.getString("userId", null)
    }

    fun getUserName(): String {
        return prefs.getString("user_name", "User") ?: "User"
    }

    fun getUserEmail(): String? {
        return prefs.getString("user_email", null)
    }

    fun clearSession() {
        prefs.edit { clear() }
    }
}