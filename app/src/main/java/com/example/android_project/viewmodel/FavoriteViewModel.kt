package com.example.android_project.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_project.data.FavoriteRepository
import com.example.android_project.data.local.FavoriteDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = FavoriteDatabase.getDatabase(application).favoriteDao()
    private val repository = FavoriteRepository(dao)

    val favoriteCards = repository.getAllFavorites().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
}