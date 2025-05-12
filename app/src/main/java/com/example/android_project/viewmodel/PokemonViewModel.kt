package com.example.android_project.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_project.data.PokemonCard
import com.example.android_project.data.PokemonCardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
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

    init {
        viewModelScope.launch {
            delay(1500)
            _cards.value = repository.getCards()
            _isLoading.value = false
        }
    }

    fun loadCardById(id: String) {
        viewModelScope.launch {
            try {
                _selectedCard.value = repository.getCardById(id)
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Failed to load card by id", e)
            }
        }
    }

    fun searchCardsByName(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _isSearchActive.value = true
            _searchQuery.value = name

            try {
                if (name.isBlank()) {
                    _cards.value = emptyList()
                } else {
                    _cards.value = repository.getCardsByName(name)
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Search failed", e)
                _cards.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetCards() {
        viewModelScope.launch {
            _isLoading.value = true
            _cards.value = repository.getCards()
            _isLoading.value = false
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}