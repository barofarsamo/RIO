package com.riyobox.data.model

data class Download(
    val id: String,
    val movieId: String,
    val movieTitle: String,
    val thumbnailUrl: String,
    val fileSize: Int, // in MB
    val quality: String,
    val downloadedAt: String,
    val downloadPath: String,
    val isCompleted: Boolean,
    val progress: Int // percentage
)
