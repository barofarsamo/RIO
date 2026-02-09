package com.riyobox.data.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val profilePicture: String? = null,
    val subscriptionPlan: String = "free", // free, premium
    val watchHistory: List<WatchHistoryItem> = emptyList(),
    val favorites: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class WatchHistoryItem(
    val movieId: String,
    val watchedAt: Long,
    val progress: Int // percentage
)

data class AuthResponse(
    val success: Boolean,
    val token: String? = null,
    val user: User? = null,
    val message: String? = null
)
