package com.riyobox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riyobox.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(authRepository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = authRepository.login(email, password)
            _isLoading.value = false
            
            result.fold(
                onSuccess = { token ->
                    _isLoggedIn.value = true
                    onSuccess()
                },
                onFailure = { error ->
                    _error.value = error.message
                }
            )
        }
    }
    
    fun register(email: String, password: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = authRepository.register(email, password, name)
            _isLoading.value = false
            
            result.fold(
                onSuccess = { token ->
                    _isLoggedIn.value = true
                    onSuccess()
                },
                onFailure = { error ->
                    _error.value = error.message
                }
            )
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
