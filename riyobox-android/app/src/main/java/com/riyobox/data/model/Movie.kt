package com.riyobox.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val duration: Int, // minutes
    val releaseYear: Int,
    val rating: Double,
    val categories: List<String>,
    val actors: List<String>,
    val director: String,
    val views: Long,
    val downloads: Long,
    val isFeatured: Boolean = false,
    val isSomaliOriginal: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class MovieResponse(
    val success: Boolean,
    val data: List<Movie>,
    val message: String? = null
)
