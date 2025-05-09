package com.example.android_project.data

object PokemonCardRepository {
    val cards = listOf(
        PokemonCard(
            name = "Pikachu",
            pokemon = "Stage 0",
            type = "https://pokemontcg.guru/assets/lightning-732a70ef2e2dab4cc564fbf4d85cad48b0ac9ece462be3d42166a6fea4085773.png",
            hp = "60",
            weakness = "https://pokemontcg.guru/assets/fighting-5fcb6e1f157032efac4f6830d88759e83e66530354a297b112fff24c152e8d3c.png",
            retreat = "https://pokemontcg.guru/assets/colorless-fd5125f53e603b89cdfc6984ab4cea8e3ed43323a8d84bee6a774db1ea8c3dae.png",
            series = "A",
            attackName = "Thunder Shock",
            attackDamage = "20",
            attackElement = "https://pokemontcg.guru/assets/lightning-732a70ef2e2dab4cc564fbf4d85cad48b0ac9ece462be3d42166a6fea4085773.png",
            imageUrls = "https://images.pokemontcg.io/xy3/27_hires.png",
            attackDescription = "flip a coin. if heads, your opponent's Active Pokemon is now Paralyzed."
        ),
        PokemonCard(
            name = "Charmander",
            pokemon = "Stage 0",
            type = "https://pokemontcg.guru/assets/fire-76e636965a1e28800904de4abbf84ade3b019bbbce7021987f379971f881c2b5.png",
            hp = "70",
            weakness = "https://pokemontcg.guru/assets/water-6b0bc3ea40b358d372e8be04aa90be9fb74e3e46ced6824f6b264cc2a7c7e32a.png",
            retreat = "https://pokemontcg.guru/assets/colorless-fd5125f53e603b89cdfc6984ab4cea8e3ed43323a8d84bee6a774db1ea8c3dae.png",
            series = "A",
            attackName = "Ember",
            attackDamage = "30",
            attackElement = "https://pokemontcg.guru/assets/fire-76e636965a1e28800904de4abbf84ade3b019bbbce7021987f379971f881c2b5.png",
            imageUrls = "https://images.pokemontcg.io/sm9/12_hires.png",
            attackDescription = " Discard an Energy from this Pokemon.",
        )
    )
}