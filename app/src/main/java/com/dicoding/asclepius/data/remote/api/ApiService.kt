package com.dicoding.asclepius.data.remote.api

import com.dicoding.asclepius.data.remote.model.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getLatestCancerNews(
        @Query("q") query: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ): ResponseBody
}