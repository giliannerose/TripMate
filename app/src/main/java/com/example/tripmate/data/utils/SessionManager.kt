package com.example.tripmate.data.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("TripMatePrefs", Context.MODE_PRIVATE)

    companion object {
        const val NO_USER = -1
    }

    fun saveUserSession(userId: Int, userName: String, userEmail: String) {
        prefs.edit {
            putInt("userId", userId)
            putString("user_name", userName)
            putString("user_email", userEmail)
        }
    }

    fun getUserId(): Int {
        return prefs.getInt("userId", -1)
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
