package com.example.android_project.data


data class CardResponse(
    val data: List<PokemonCard>
)

data class SingleCardResponse(
    val data: PokemonCard
)

data class PokemonCard(
    val id: String,
    val name: String,
    val subtypes: List<String>,
    val types: List<String>,
    val hp: String,
    val weaknesses: List<HashMap<String, String>>,
    val retreatCost: List<String>,
    val images: CardImages,
    val set: SetInfo,
    val attacks: List<AttackInfo>
)

data class CardImages(
    val small: String,
    val large: String
)

data class SetInfo(
    val series: String
)

data class AttackInfo(
    val name: String,
    val cost: List<String>,
    val damage: String,
    val text: String
)


