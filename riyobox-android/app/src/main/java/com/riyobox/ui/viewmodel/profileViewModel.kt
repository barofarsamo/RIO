package com.riyobox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePicture: String? = null,
    val subscriptionPlan: String = "free"
)

data class UserStats(
    val watchTime: Int = 45,
    val favoriteCount: Int = 12,
    val downloadCount: Int = 8
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()
    
    private val _stats = MutableStateFlow(UserStats())
    val stats: StateFlow<UserStats> = _stats.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Simulate network delay
                delay(1000)
                
                // Mock user data - replace with API call
                val mockUser = User(
                    id = "1",
                    name = "Ahmed Hassan",
                    email = "ahmed@example.com",
                    subscriptionPlan = "premium"
                )
                
                _user.value = mockUser
                _stats.value = UserStats()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load profile"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
