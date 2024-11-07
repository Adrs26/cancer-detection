package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.Check
import com.dicoding.asclepius.data.repository.CheckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val checkRepository: CheckRepository) : ViewModel() {
    private val _checkHistory = MutableLiveData<List<Check>>()
    val checkHistory: LiveData<List<Check>> = _checkHistory

    fun getCheckHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val checkHistory = checkRepository.getAllCheck()
            _checkHistory.postValue(checkHistory)
        }
    }

    fun saveCheck(check: Check) {
        viewModelScope.launch(Dispatchers.IO) {
            checkRepository.insertCheck(check)
        }
    }
}