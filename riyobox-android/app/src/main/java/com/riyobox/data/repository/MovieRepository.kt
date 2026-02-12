package com.riyobox.data.repository

import com.riyobox.data.local.dao.MovieDao
import com.riyobox.data.model.Movie
import com.riyobox.data.network.ApiService
import com.riyobox.data.network.ApiResponse
import com.riyobox.data.network.PageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao
) {
    
    suspend fun getMovies(
        token: String,
        page: Int = 0,
        size: Int = 20
    ): Result<PageResponse<Movie>> {
        return try {
            val response = apiService.getMovies(
                token = "Bearer $token",
                page = page,
                size = size
            )
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                
                if (apiResponse?.success == true && apiResponse.data != null) {
                    // Cache movies locally
                    movieDao.insertMovies(apiResponse.data.content)
                    
                    Result.success(apiResponse.data)
                } else {
                    // Fallback to local database
                    val localMovies = movieDao.getAllMovies()
                    val fallbackResponse = PageResponse(
                        content = localMovies,
                        page = page,
                        size = size,
                        totalElements = localMovies.size.toLong(),
                        totalPages = 1,
                        last = true
                    )
                    Result.success(fallbackResponse)
                }
            } else {
                // Network error - use local database
                val localMovies = movieDao.getAllMovies()
                val fallbackResponse = PageResponse(
                    content = localMovies,
                    page = page,
                    size = size,
                    totalElements = localMovies.size.toLong(),
                    totalPages = 1,
                    last = true
                )
                Result.success(fallbackResponse)
            }
        } catch (e: Exception) {
            // Any exception - use local database
            val localMovies = movieDao.getAllMovies()
            val fallbackResponse = PageResponse(
                content = localMovies,
                page = page,
                size = size,
                totalElements = localMovies.size.toLong(),
                totalPages = 1,
                last = true
            )
            Result.success(fallbackResponse)
        }
    }
    
    suspend fun getMovieById(token: String, id: String): Result<Movie> {
        return try {
            // Try local first
            val localMovie = movieDao.getMovieById(id)
            if (localMovie != null) {
                return Result.success(localMovie)
            }
            
            // Try network
            val response = apiService.getMovieById(
                token = "Bearer $token",
                id = id
            )
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                
                if (apiResponse?.success == true && apiResponse.data != null) {
                    // Cache it
                    movieDao.insertMovie(apiResponse.data)
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(
                        Exception(apiResponse?.message ?: "Movie not found")
                    )
                }
            } else {
                Result.failure(Exception("Network error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTrendingMovies(token: String): Result<List<Movie>> {
        return try {
            val response = apiService.getTrendingMovies("Bearer $token")
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                
                if (apiResponse?.success == true) {
                    val movies = apiResponse.data ?: emptyList()
                    movieDao.insertMovies(movies)
                    Result.success(movies)
                } else {
                    Result.failure(
                        Exception(apiResponse?.message ?: "Failed to load trending")
                    )
                }
            } else {
                // Fallback to local
                val localMovies = movieDao.getAllMovies().take(10)
                Result.success(localMovies)
            }
        } catch (e: Exception) {
            val localMovies = movieDao.getAllMovies().take(10)
            Result.success(localMovies)
        }
    }
    
    suspend fun getFeaturedMovies(token: String): Result<List<Movie>> {
        return try {
            val response = apiService.getFeaturedMovies("Bearer $token")
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                
                if (apiResponse?.success == true) {
                    val movies = apiResponse.data ?: emptyList()
                    // Cache featured movies
                    movies.forEach { movie ->
                        movieDao.insertMovie(movie.copy(isFeatured = true))
                    }
                    Result.success(movies)
                } else {
                    Result.failure(
                        Exception(apiResponse?.message ?: "Failed to load featured")
                    )
                }
            } else {
                // Fallback to local featured movies
                val localFeatured = movieDao.getFeaturedMovies()
                Result.success(localFeatured)
            }
        } catch (e: Exception) {
            val localFeatured = movieDao.getFeaturedMovies()
            Result.success(localFeatured)
        }
    }
    
    suspend fun searchMovies(token: String, query: String): Result<List<Movie>> {
        return try {
            val response = apiService.searchMovies(
                token = "Bearer $token",
                query = query
            )
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                
                if (apiResponse?.success == true) {
                    val movies = apiResponse.data?.content ?: emptyList()
                    Result.success(movies)
                } else {
                    Result.failure(
                        Exception(apiResponse?.message ?: "Search failed")
                    )
                }
            } else {
                // Fallback to local search
                val localMovies = movieDao.getAllMovies()
                val filtered = localMovies.filter { movie ->
                    movie.title.contains(query, ignoreCase = true) ||
                    movie.description.contains(query, ignoreCase = true) ||
                    movie.categories.any { it.contains(query, ignoreCase = true) }
                }
                Result.success(filtered)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
