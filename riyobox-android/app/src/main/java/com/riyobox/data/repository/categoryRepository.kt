package com.riyobox.data.repository

import com.riyobox.data.model.Category
import com.riyobox.data.network.ApiService
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse?.message ?: "Failed to load categories"))
                }
            } else {
                Result.failure(Exception("Network error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
