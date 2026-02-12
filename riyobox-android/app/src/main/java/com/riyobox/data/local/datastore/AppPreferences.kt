package com.riyobox.data.local.datastore

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    fun getAuthToken(): String? = null
    fun saveAuthToken(token: String) {}
    fun clear() {}
}
