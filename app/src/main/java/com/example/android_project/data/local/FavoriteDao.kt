package com.example.android_project.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: FavoritePokemonCard)

    @Delete
    suspend fun delete(card: FavoritePokemonCard)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoritePokemonCard>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Query("SELECT * FROM favorites WHERE id = :id LIMIT 1")
    suspend fun getCardById(id: String): FavoritePokemonCard?
}