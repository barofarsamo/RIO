package com.riyobox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riyobox.data.model.Movie
import com.riyobox.data.repository.MovieRepository
import com.riyobox.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private var currentPage = 0
    var isLastPage = false
    
    fun loadMovies(refresh: Boolean = false) {
        if (isLoading.value) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            if (refresh) {
                currentPage = 0
                isLastPage = false
                _movies.value = emptyList()
            }
            
            if (isLastPage) {
                _isLoading.value = false
                return@launch
            }
            
            val token = authRepository.getAuthToken()
            if (token == null) {
                _error.value = "Not authenticated. Please login."
                _isLoading.value = false
                return@launch
            }
            
            try {
                // ✅ CORRECT: Use proper repository function
                val result = movieRepository.getMovies(
                    token = token,
                    page = currentPage,
                    size = 20
                )
                
                if (result.isSuccess) {
                    val pageResponse = result.getOrThrow()
                    val newMovies = pageResponse.content
                    
                    _movies.value = if (currentPage == 0) {
                        newMovies
                    } else {
                        _movies.value + newMovies
                    }
                    
                    currentPage++
                    isLastPage = pageResponse.last
                    
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Failed to load movies"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadMovieById(id: String, onSuccess: (Movie) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val token = authRepository.getAuthToken()
            if (token == null) {
                onError("Not authenticated")
                _isLoading.value = false
                return@launch
            }
            
            try {
                val result = movieRepository.getMovieById(token, id)
                
                if (result.isSuccess) {
                    onSuccess(result.getOrThrow())
                } else {
                    onError(result.exceptionOrNull()?.message ?: "Movie not found")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
