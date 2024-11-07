package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.api.ApiClient
import com.dicoding.asclepius.data.remote.model.Articles
import com.dicoding.asclepius.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val newsRepository = NewsRepository(ApiClient.apiClient)

    private val _newsArticles = MutableLiveData<List<Articles>>()
    val newsArticles: LiveData<List<Articles>> = _newsArticles

    private val _exception = MutableLiveData<Boolean>()
    val exception: LiveData<Boolean> = _exception

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getLatestCancerNews() {
        if (_newsArticles.value == null) {
            _isLoading.postValue(true)

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val newsArticles = newsRepository.getLatestCancerNews(
                        "cancer",
                        "health",
                        "en",
                        BuildConfig.API_KEY
                    ).articles
                    _newsArticles.postValue(newsArticles)
                } catch (e: Exception) {
                    _exception.postValue(true)
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun resetExceptionValue() {
        _exception.value = false
    }
}