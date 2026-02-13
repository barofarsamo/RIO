package com.riyobox.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {
    fun recordWatch(movieId: String) {}
    fun savePlaybackProgress(movieId: String, position: Long) {}
}
