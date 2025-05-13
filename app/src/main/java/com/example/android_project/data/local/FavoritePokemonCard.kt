package com.example.android_project.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoritePokemonCard(
    @PrimaryKey val id: String,
    val name: String,
    val subtypes: String,
    val types: String,
    val hp: String,
    val weaknesses: String,
    val retreatCost: String,
    val imageSmall: String,
    val imageLarge: String,
    val series: String,
    val attackName: String,
    val attackCost: String,
    val attackDamage: String,
    val attackText: String
)
