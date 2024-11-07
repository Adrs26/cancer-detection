package com.dicoding.asclepius.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheckViewModel : ViewModel() {
    private val _userPhoto = MutableLiveData<Uri>()
    val userPhoto: LiveData<Uri> = _userPhoto

    fun getUserPhoto(userPhoto: Uri) {
        _userPhoto.value = userPhoto
    }
}