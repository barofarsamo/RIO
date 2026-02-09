package com.riyobox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riyobox.data.model.Movie
import com.riyobox.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    
    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()
    
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private var searchJob: Job? = null
    
    init {
        // Load recent searches from preferences (mock for now)
        _recentSearches.value = listOf(
            "Somali",
            "Action",
            "Comedy",
            "Drama"
        )
    }
    
    fun searchMovies(query: String) {
        searchJob?.cancel()
        
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }
        
        searchJob = viewModelScope.launch {
            _isLoading.value = true
            
            // Add delay for debouncing
            delay(300)
            
            try {
                // Mock search results - replace with API call
                val mockMovies = listOf(
                    Movie(
                        id = "1",
                        title = "Hooyo Macaan",
                        description = "A heartwarming Somali family drama",
                        thumbnailUrl = "https://picsum.photos/200/300?random=1",
                        videoUrl = "",
                        duration = 120,
                        releaseYear = 2023,
                        rating = 4.5,
                        categories = listOf("Drama", "Family"),
                        actors = emptyList(),
                        director = "",
                        views = 1000,
                        downloads = 500,
                        isSomaliOriginal = true
                    ),
                    Movie(
                        id = "2",
                        title = "Mogadishu Nights",
                        description = "Action thriller set in Mogadishu",
                        thumbnailUrl = "https://picsum.photos/200/300?random=2",
                        videoUrl = "",
                        duration = 110,
                        releaseYear = 2022,
                        rating = 4.2,
                        categories = listOf("Action", "Thriller"),
                        actors = emptyList(),
                        director = "",
                        views = 1500,
                        downloads = 700,
                        isSomaliOriginal = true
                    )
                ).filter { 
                    it.title.contains(query, ignoreCase = true) || 
                    it.categories.any { category -> 
                        category.contains(query, ignoreCase = true) 
                    }
                }
                
                _searchResults.value = mockMovies
                
                // Add to recent searches
                if (query.isNotBlank() && !_recentSearches.value.contains(query)) {
                    val updatedSearches = listOf(query) + _recentSearches.value.take(9)
                    _recentSearches.value = updatedSearches
                }
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearSearch() {
        _searchResults.value = emptyList()
    }
    
    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
    }
}
