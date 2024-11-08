package com.submission.aplikasidicodingevent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (favoriteEvent: FavoriteEvent)

    @Delete
    fun delete (favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent ORDER BY id ASC")
    fun getAllFavorite(): LiveData<List<FavoriteEvent>>

    // Fungsi untuk mendapatkan event favorit berdasarkan ID
    @Query("SELECT * FROM FavoriteEvent WHERE id = :eventId LIMIT 1")
    fun getFavoriteById(eventId: Int): LiveData<FavoriteEvent?>
}