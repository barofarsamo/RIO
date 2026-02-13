package com.riyobox.data.repository

import com.riyobox.data.network.ApiService
import com.riyobox.data.network.ApiResponse
import com.riyobox.data.local.datastore.AppPreferences
import com.riyobox.data.model.User
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferences: AppPreferences
) {
    
    suspend fun register(email: String, password: String, name: String): Result<User> {
        return try {
            val response = apiService.register(
                com.riyobox.data.network.RegisterRequest(
                    email = email,
                    password = password,
                    name = name
                )
            )
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                
                if (apiResponse?.success == true && apiResponse.data != null) {
                    val authResponse = apiResponse.data
                    val token = authResponse.token
                    val user = authResponse.user
                    
                    preferences.saveAuthToken(token)
                    preferences.saveUser(user)
                    Result.success(user)
                } else {
                    Result.failure(
                        Exception(apiResponse?.message ?: "Registration failed")
                    )
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Network error: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = apiService.login(
                com.riyobox.data.network.LoginRequest(
                    email = email,
                    password = password
                )
            )
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                
                if (apiResponse?.success == true && apiResponse.data != null) {
                    val authResponse = apiResponse.data
                    val token = authResponse.token
                    val user = authResponse.user
                    
                    preferences.saveAuthToken(token)
                    preferences.saveUser(user)
                    Result.success(user)
                } else {
                    Result.failure(
                        Exception(apiResponse?.message ?: "Login failed")
                    )
                }
            } else {
                when (response.code()) {
                    401 -> Result.failure(Exception("Invalid email or password"))
                    403 -> Result.failure(Exception("Account disabled"))
                    404 -> Result.failure(Exception("User not found"))
                    else -> {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        Result.failure(Exception("Network error: $errorBody"))
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logout() {
        preferences.clearAuthToken()
        preferences.clearUser()
    }
    
    fun getAuthToken(): String? {
        return preferences.getAuthToken()
    }
    
    fun getCurrentUser(): User? {
        return preferences.getUser()
    }
    
    fun isLoggedIn(): Boolean {
        return preferences.getAuthToken() != null
    }
    
    suspend fun refreshToken(): Boolean {
        return try {
            val response = apiService.refreshToken()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data?.token != null) {
                    preferences.saveAuthToken(apiResponse.data.token)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
