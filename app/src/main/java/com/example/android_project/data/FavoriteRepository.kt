package com.example.android_project.data

import com.example.android_project.data.local.FavoriteDao
import com.example.android_project.data.local.FavoritePokemonCard
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val dao: FavoriteDao) {
    fun getAllFavorites(): Flow<List<FavoritePokemonCard>> = dao.getAllFavorites()
}