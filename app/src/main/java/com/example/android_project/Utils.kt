package com.example.android_project

val typeIconMap = mapOf(
    "Fire" to "https://pokemontcg.guru/assets/fire-76e636965a1e28800904de4abbf84ade3b019bbbce7021987f379971f881c2b5.png",
    "Water" to "https://pokemontcg.guru/assets/water-6b0bc3ea40b358d372e8be04aa90be9fb74e3e46ced6824f6b264cc2a7c7e32a.png",
    "Grass" to "https://pokemontcg.guru/assets/grass-ec3509d75db6cd146139044107045ccb5bcbb528b02c3de89d709a7be4a0bf90.png",
    "Metal" to "https://pokemontcg.guru/assets/metal-076b10c3700a68913c376f841b46a1d63c3895247385b4360bc70739289179b7.png",
    "Lightning" to "https://pokemontcg.guru/assets/lightning-732a70ef2e2dab4cc564fbf4d85cad48b0ac9ece462be3d42166a6fea4085773.png",
    "Dragon" to "https://archives.bulbagarden.net/media/upload/7/70/Dragon_icon_SwSh.png",
    "Psychic" to "https://pokemontcg.guru/assets/psychic-503107a3ed9d9cce58e290677918f057ea6dc4e75042f2a627a5dd8a8bf6af9e.png",
    "Colorless" to "https://pokemontcg.guru/assets/colorless-fd5125f53e603b89cdfc6984ab4cea8e3ed43323a8d84bee6a774db1ea8c3dae.png",
    "Fighting" to "https://pokemontcg.guru/assets/fighting-5fcb6e1f157032efac4f6830d88759e83e66530354a297b112fff24c152e8d3c.png",
    "Darkness" to "https://pokemontcg.guru/assets/darkness-d766bdc83589235f104c3c3892cff4de80048e7a729f24b6e5e53a1838c7ebfa.png",
    "Fairy" to "https://pokemontcg.guru/assets/fairy-aa8ddf0f7c5b65b6d01c40652c1661b706720feaf8e3f1ffa64f7b486439bb08.png",
)


fun extractTypeWords(list: List<Any?>): List<String> {
    return list.mapNotNull {
        when (it) {
            is Map<*, *> -> it["type"]?.toString()
            is String -> it
            else -> null
        }
    }
}