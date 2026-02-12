package com.riyobox.di

import android.content.Context
import com.riyobox.data.local.dao.MovieDao
import com.riyobox.data.local.database.AppDatabase
import com.riyobox.data.local.datastore.AppPreferences
import com.riyobox.data.network.ApiService
import com.riyobox.data.network.RetrofitClient
import com.riyobox.data.repository.AuthRepository
import com.riyobox.data.repository.CategoryRepository
import com.riyobox.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)
    }
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
    
    @Provides
    @Singleton
    fun provideMovieDao(database: AppDatabase) = database.movieDao()
    
    @Provides
    @Singleton
    fun provideRetrofitClient(
        @ApplicationContext context: Context,
        preferences: AppPreferences
    ): RetrofitClient {
        return RetrofitClient(context, preferences)
    }
    
    @Provides
    @Singleton
    fun provideApiService(retrofitClient: RetrofitClient) = retrofitClient.apiService
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        preferences: AppPreferences
    ): AuthRepository {
        return AuthRepository(apiService, preferences)
    }
    
    @Provides
    @Singleton
    fun provideMovieRepository(
        apiService: ApiService,
        movieDao: MovieDao
    ): MovieRepository {
        return MovieRepository(apiService, movieDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        apiService: ApiService
    ): CategoryRepository {
        return CategoryRepository(apiService)
    }
}
