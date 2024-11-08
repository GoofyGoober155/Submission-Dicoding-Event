package com.submission.aplikasidicodingevent.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.submission.aplikasidicodingevent.database.FavoriteDao
import com.submission.aplikasidicodingevent.database.FavoriteEvent
import com.submission.aplikasidicodingevent.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository (application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEvent>> = mFavoritesDao.getAllFavorite()

    fun insert (favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoritesDao.insert(favoriteEvent) }
    }

    fun delete (favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoritesDao.delete(favoriteEvent) }
    }

    // Fungsi untuk mendapatkan daftar event favorit
    fun isFavorite(eventId: Int): LiveData<FavoriteEvent?> {
        return mFavoritesDao.getFavoriteById(eventId)
    }
}