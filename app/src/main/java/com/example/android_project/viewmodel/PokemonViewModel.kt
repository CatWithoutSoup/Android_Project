package com.example.android_project.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_project.FilterPreferences
import com.example.android_project.data.PokemonCard
import com.example.android_project.data.PokemonCardRepository
import com.example.android_project.data.local.FavoriteDatabase
import com.example.android_project.data.toPokemonCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private val appContext = application.applicationContext
    private val repository = PokemonCardRepository
    private val _cards = MutableStateFlow<List<PokemonCard>>(emptyList())
    val cards: StateFlow<List<PokemonCard>> = _cards

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive

    private val _selectedCard = MutableStateFlow<PokemonCard?>(null)
    val selectedCard: StateFlow<PokemonCard?> = _selectedCard

    private val _isFiltersDialogVisible = MutableStateFlow(false)
    val isFiltersDialogVisible: StateFlow<Boolean> = _isFiltersDialogVisible

    private val _hasBadge = MutableStateFlow(false)
    val hasBadge: StateFlow<Boolean> = _hasBadge


    private val _selectedFilters = MutableStateFlow<Set<String>>(emptySet())
    val selectedFilters: StateFlow<Set<String>> = _selectedFilters

    private val filterPrefs = FilterPreferences(application.applicationContext)

    init {
        viewModelScope.launch {
            val savedFilters = filterPrefs.selectedFilters.first()
            _selectedFilters.value = savedFilters
            _hasBadge.value = savedFilters.isNotEmpty()

            searchCardsByName()
            delay(1500)
        }
    }

    fun loadCardById(id: String) {
        viewModelScope.launch {
            try {
                val dao = FavoriteDatabase.getDatabase(appContext).favoriteDao()
                val local = dao.getCardById(id)
                _selectedCard.value = local?.toPokemonCard() ?: repository.getCardById(id)
            } catch (e: Exception) {
                Log.e("VM", "Card load failed", e)
            }
        }
    }

    fun searchCardsByName(query: String = _searchQuery.value) {
        viewModelScope.launch {
            _isLoading.value = true
            _isSearchActive.value = true
            _searchQuery.value = query

            try {
                val nameQuery = query.takeIf { it.isNotBlank() }?.let { "name:$it" }
                val filterQuery = _selectedFilters.value.takeIf { it.isNotEmpty() }
                    ?.joinToString(" OR ") { "types:$it" }

                val finalQuery = listOfNotNull(nameQuery, filterQuery).joinToString(" ")

                Log.d("PokemonViewModel", "Final query: $finalQuery")

                _cards.value = if (finalQuery.isBlank()) {
                    repository.getCards()
                } else {
                    repository.getCardsByName(finalQuery)
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Search failed", e)
                _cards.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun onFiltersClicked() {
        _isFiltersDialogVisible.value = !_isFiltersDialogVisible.value
    }

    fun applySelectedFilters() {
        viewModelScope.launch {
            filterPrefs.saveFilters(_selectedFilters.value)
        }
        _isFiltersDialogVisible.value = false
        _hasBadge.value = _selectedFilters.value.isNotEmpty()
        searchCardsByName()
    }

    fun toggleFilter(filter: String) {
        _selectedFilters.value = if (_selectedFilters.value.contains(filter)) {
            _selectedFilters.value - filter
        } else {
            _selectedFilters.value + filter
        }
    }
}
