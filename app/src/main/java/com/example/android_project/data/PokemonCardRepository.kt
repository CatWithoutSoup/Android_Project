package com.example.android_project.data

import com.example.android_project.api.RetrofitInstance.api

object PokemonCardRepository {
    suspend fun getCards(): List<PokemonCard> {
        return api.getCards().data
    }

    suspend fun getCardById(id: String): PokemonCard {
        return api.getCardById(id).data
    }

    suspend fun getCardsByName(name: String): List<PokemonCard> {
        return api.getCardsByName("name:$name").data
    }
}