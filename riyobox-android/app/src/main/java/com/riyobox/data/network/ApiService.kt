package com.riyobox.data.network

import com.riyobox.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponse>>
    
    @GET("movies")
    suspend fun getMovies(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "createdAt,desc"
    ): Response<ApiResponse<PageResponse<Movie>>>
    
    @GET("movies/all")
    suspend fun getAllMovies(): Response<ApiResponse<List<Movie>>>
    
    @GET("movies/trending")
    suspend fun getTrendingMovies(
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<Movie>>>
    
    @GET("movies/featured")
    suspend fun getFeaturedMovies(): Response<ApiResponse<List<Movie>>>
    
    @GET("movies/{id}")
    suspend fun getMovieById(
        @Path("id") id: String
    ): Response<ApiResponse<Movie>>
    
    @GET("movies/search")
    suspend fun searchMovies(
        @Query("q") query: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<PageResponse<Movie>>>
    
    @POST("movies/{id}/watch")
    suspend fun recordWatch(
        @Path("id") id: String,
        @Body request: WatchProgressRequest
    ): Response<ApiResponse<Void>>
    
    @GET("categories")
    suspend fun getCategories(): Response<ApiResponse<List<Category>>>
    
    @GET("categories/{id}/movies")
    suspend fun getCategoryMovies(
        @Path("id") id: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<PageResponse<Movie>>>

    @POST("auth/refresh")
    suspend fun refreshToken(): Response<ApiResponse<AuthResponse>>
}

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)

data class WatchProgressRequest(
    val position: Long,
    val duration: Long,
    val percentage: Int
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: com.riyobox.data.model.User
)

data class RefreshTokenRequest(
    val token: String
)
