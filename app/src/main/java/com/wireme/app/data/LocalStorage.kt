package com.wireme.app.data

import android.content.Context
import android.content.SharedPreferences

class LocalStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("WireMe", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun saveEmail(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getEmail(): String? {
        return prefs.getString("email", null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
