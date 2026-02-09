package com.riyobox.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riyobox.data.model.Movie

@Dao
interface MovieDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)
    
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>
    
    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: String): Movie?
    
    @Query("SELECT * FROM movies WHERE isFeatured = 1")
    suspend fun getFeaturedMovies(): List<Movie>
    
    @Query("SELECT * FROM movies WHERE isSomaliOriginal = 1")
    suspend fun getSomaliOriginals(): List<Movie>
    
    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}
