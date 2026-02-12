package com.riyobox.data.local.datastore

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.riyobox.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("riyobox_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER = "user_profile"
    }

    fun getAuthToken(): String? = prefs.getString(KEY_AUTH_TOKEN, null)

    fun saveAuthToken(token: String) {
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun clearAuthToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    fun saveUser(user: User) {
        val json = gson.toJson(user)
        prefs.edit().putString(KEY_USER, json).apply()
    }

    fun getUser(): User? {
        val json = prefs.getString(KEY_USER, null)
        return if (json != null) {
            try {
                gson.fromJson(json, User::class.java)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    fun clearUser() {
        prefs.edit().remove(KEY_USER).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
