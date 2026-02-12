package com.riyobox.data.local.datastore

import android.content.Context

class AppPreferences(context: Context) {
    fun getAuthToken(): String? = null
    fun clearAuthToken() {}
    fun saveAuthToken(token: String) {}
    fun saveUser(user: com.riyobox.data.model.User) {}
    fun clearUser() {}
    fun getUser(): com.riyobox.data.model.User? = null
}
