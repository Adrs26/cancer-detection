package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.repository.CheckRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val checkRepository: CheckRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(checkRepository) as T
    }
}