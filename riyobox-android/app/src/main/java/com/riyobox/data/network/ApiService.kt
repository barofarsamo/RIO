package com.riyobox.data.network

import com.riyobox.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // ✅ FIXED: Authentication with proper error handling
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponse>>
    
    // ✅ FIXED: Movies with proper pagination
    @GET("movies")
    suspend fun getMovies(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "createdAt,desc"
    ): Response<ApiResponse<PageResponse<Movie>>>
    
    @GET("movies/all")
    suspend fun getAllMovies(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Movie>>>
    
    @GET("movies/trending")
    suspend fun getTrendingMovies(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<Movie>>>
    
    @GET("movies/featured")
    suspend fun getFeaturedMovies(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Movie>>>
    
    @GET("movies/{id}")
    suspend fun getMovieById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApiResponse<Movie>>
    
    // ✅ FIXED: Search with pagination
    @GET("movies/search")
    suspend fun searchMovies(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<PageResponse<Movie>>>
    
    // ✅ FIXED: Watch progress
    @POST("movies/{id}/watch")
    suspend fun recordWatch(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: WatchProgressRequest
    ): Response<ApiResponse<Void>>
    
    // ✅ FIXED: Categories
    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Category>>>
    
    @GET("categories/{id}/movies")
    suspend fun getCategoryMovies(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<PageResponse<Movie>>>
}

// ✅ ADD: Standard API response wrapper
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val timestamp: Long = System.currentTimeMillis()
)

// ✅ ADD: Page response for pagination
data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)

// ✅ ADD: Watch progress request
data class WatchProgressRequest(
    val position: Long, // in milliseconds
    val duration: Long, // in milliseconds
    val percentage: Int // 0-100
)

// ✅ KEEP existing RegisterRequest and LoginRequest
