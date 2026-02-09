package com.riyobox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riyobox.data.model.Download
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class DownloadSettings(
    val wifiOnly: Boolean = true,
    val quality: String = "720p",
    val autoDelete: Boolean = false
)

data class StorageInfo(
    val usedGB: Double = 2.3,
    val totalGB: Double = 8.0,
    val usedPercentage: Int = 29
)

@HiltViewModel
class DownloadsViewModel @Inject constructor() : ViewModel() {
    
    private val _downloads = MutableStateFlow<List<Download>>(emptyList())
    val downloads: StateFlow<List<Download>> = _downloads.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _storageInfo = MutableStateFlow(StorageInfo())
    val storageInfo: StateFlow<StorageInfo> = _storageInfo.asStateFlow()
    
    private val _settings = MutableStateFlow(DownloadSettings())
    val settings: StateFlow<DownloadSettings> = _settings.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadDownloads()
    }
    
    fun loadDownloads() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                // Mock data - replace with real database/API calls
                val mockDownloads = listOf(
                    Download(
                        id = "1",
                        movieId = "1",
                        movieTitle = "Hooyo Macaan",
                        thumbnailUrl = "https://picsum.photos/200/300?random=1",
                        fileSize = 1250,
                        quality = "720p",
                        downloadedAt = formatDate(Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L)), // 2 days ago
                        downloadPath = "",
                        isCompleted = true,
                        progress = 100
                    ),
                    Download(
                        id = "2",
                        movieId = "2",
                        movieTitle = "Mogadishu Nights",
                        thumbnailUrl = "https://picsum.photos/200/300?random=2",
                        fileSize = 850,
                        quality = "480p",
                        downloadedAt = formatDate(Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L)), // 1 week ago
                        downloadPath = "",
                        isCompleted = true,
                        progress = 100
                    )
                )
                
                _downloads.value = mockDownloads
                
                // Calculate storage usage
                val totalSize = mockDownloads.sumOf { it.fileSize } / 1024.0 // Convert to GB
                val percentage = ((totalSize / _storageInfo.value.totalGB) * 100).toInt()
                
                _storageInfo.value = StorageInfo(
                    usedGB = totalSize,
                    totalGB = _storageInfo.value.totalGB,
                    usedPercentage = percentage
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load downloads"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteDownload(id: String) {
        viewModelScope.launch {
            _downloads.value = _downloads.value.filter { it.id != id }
            // Update storage info
            loadDownloads()
        }
    }
    
    fun deleteAllDownloads() {
        viewModelScope.launch {
            _downloads.value = emptyList()
            _storageInfo.value = StorageInfo(usedGB = 0.0, usedPercentage = 0)
        }
    }
    
    fun updateWifiOnly(wifiOnly: Boolean) {
        _settings.value = _settings.value.copy(wifiOnly = wifiOnly)
    }
    
    fun updateQuality(quality: String) {
        _settings.value = _settings.value.copy(quality = quality)
    }
    
    fun clearError() {
        _error.value = null
    }
    
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}
