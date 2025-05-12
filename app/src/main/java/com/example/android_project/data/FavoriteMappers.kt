package com.example.android_project.data

import com.example.android_project.data.local.FavoritePokemonCard

fun FavoritePokemonCard.toPokemonCard(): PokemonCard {
    return PokemonCard(
        id = id,
        name = name,
        subtypes = subtypes.split(","),
        types = types.split(","),
        hp = hp,
        weaknesses = weaknesses.split(",").map {
            hashMapOf("type" to it.trim())
        },
        retreatCost = retreatCost.split(","),
        images = CardImages(imageSmall, imageLarge),
        set = SetInfo(series),
        attacks = listOf(
            AttackInfo(
                name = attackName,
                cost = attackCost.split(","),
                damage = attackDamage,
                text = attackText
            )
        )
    )
}