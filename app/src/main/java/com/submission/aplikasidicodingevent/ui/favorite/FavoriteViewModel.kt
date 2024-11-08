package com.submission.aplikasidicodingevent.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.submission.aplikasidicodingevent.database.FavoriteEvent
import com.submission.aplikasidicodingevent.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData yang akan diobservasi oleh Fragment
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    //LiveData Database
    private val favoriteRepository = FavoriteRepository(application)

    private val _favoriteEvents = MutableLiveData<List<FavoriteEvent>>()
    val favoriteEvents: LiveData<List<FavoriteEvent>> get() = _favoriteEvents

    init {
        fetchFavoriteEvents()
    }

    //Fungsi memanggil events
    private fun fetchFavoriteEvents() {
        _isLoading.value = true
        favoriteRepository.getAllFavorites().observeForever { favorites ->
            _favoriteEvents.value = favorites
        }
    }
}