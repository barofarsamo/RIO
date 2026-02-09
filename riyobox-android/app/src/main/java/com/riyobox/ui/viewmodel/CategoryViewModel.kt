package com.riyobox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riyobox.data.model.Category
import com.riyobox.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // For now, use mock data. Replace with API call later
                val mockCategories = listOf(
                    Category("1", "Action", "Action movies", "🎬", 45),
                    Category("2", "Comedy", "Comedy movies", "😂", 32),
                    Category("3", "Drama", "Drama movies", "🎭", 67),
                    Category("4", "Horror", "Horror movies", "👻", 23),
                    Category("5", "Romance", "Romance movies", "💘", 28),
                    Category("6", "Somali Originals", "Somali movies", "🇸🇴", 18),
                    Category("7", "Documentary", "Documentary movies", "📽️", 41),
                    Category("8", "New Releases", "New movies", "🆕", 8),
                    Category("9", "International", "International movies", "🌍", 89),
                    Category("10", "Award Winners", "Award winning movies", "🏆", 12)
                )
                
                _categories.value = mockCategories
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load categories"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
