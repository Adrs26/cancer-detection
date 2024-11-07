package com.dicoding.asclepius.data.repository

import com.dicoding.asclepius.data.remote.api.ApiService
import com.dicoding.asclepius.data.remote.model.ResponseBody

class NewsRepository(private val apiService: ApiService) {
    suspend fun getLatestCancerNews(
        query: String,
        category: String,
        language: String,
        apiKey: String
    ): ResponseBody {
        return apiService.getLatestCancerNews(query, category, language, apiKey)
    }
}