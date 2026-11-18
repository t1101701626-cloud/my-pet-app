package com.example.model

enum class LottieMood {
    HAPPY,
    TICKLED,
    LOVED,
    SURPRISED,
    CHEWING,
    SLEEPY,
    SLEEPING,
    SNEEZING,
    LISTENING,
    TALKING,
    BATHING,
    BRUSHING
}

enum class WardrobeCategory(val displayName: String) {
    HATS("Sombreros"),
    OUTFITS("Ropita"),
    ACCESSORIES("Accesorios")
}

data class WardrobeItem(
    val id: String,
    val name: String,
    val category: WardrobeCategory,
    val price: Int,
    val emoji: String,
    val colorPrimary: Long,
    val colorAccent: Long,
    val description: String = ""
)

data class FoodItem(
    val id: String,
    val name: String,
    val emoji: String,
    val hungerRestore: Float,
    val happinessBonus: Float,
    val price: Int = 0
)

data class LottiePetState(
    val name: String = "Lottie",
    val hunger: Float = 85f,     // 0 to 100
    val hygiene: Float = 90f,    // 0 to 100
    val energy: Float = 80f,     // 0 to 100
    val happiness: Float = 95f,  // 0 to 100
    val bambooCoins: Int = 120,
    val isSleeping: Boolean = false,
    val equippedHatId: String? = "flower_clip",
    val equippedOutfitId: String? = "mint_romper",
    val equippedAccessoryId: String? = null,
    val unlockedItemIds: Set<String> = setOf("flower_clip", "mint_romper", "heart_glasses"),
    val hasCompletedOnboarding: Boolean = false
)

object GameCatalogs {
    val foods = listOf(
        FoodItem("bamboo_shoot", "Bambú Tierno", "🎋", 20f, 10f, 0),
        FoodItem("strawberry_pancake", "Tortitas con Frutilla", "🥞", 25f, 15f, 0),
        FoodItem("honey_milk", "Lechita con Miel", "🍼", 15f, 10f, 0),
        FoodItem("apple_slice", "Manzanita Roja", "🍎", 15f, 5f, 5),
        FoodItem("sweet_peach", "Durazno Dulce", "🍑", 18f, 10f, 10),
        FoodItem("watermelon", "Sandía Fresca", "🍉", 20f, 12f, 12),
        FoodItem("honey_cookie", "Galleta Estrella", "⭐", 20f, 20f, 15)
    )

    val wardrobe = listOf(
        // Hats
        WardrobeItem("flower_clip", "Flor de Bambú", WardrobeCategory.HATS, 0, "🌸", 0xFFFF80AB, 0xFFFF4081, "Una linda flor rosa"),
        WardrobeItem("pink_bow", "Moño Princesa", WardrobeCategory.HATS, 30, "🎀", 0xFFF06292, 0xFFE91E63, "Moño elegante y suave"),
        WardrobeItem("nightcap", "Gorro de Nube", WardrobeCategory.HATS, 45, "🌙", 0xFF90CAF9, 0xFF42A5F5, "Gorro para dulces sueños"),
        WardrobeItem("royal_crown", "Corona de Oro", WardrobeCategory.HATS, 80, "👑", 0xFFFFD54F, 0xFFFFB300, "Para la reina panda"),
        WardrobeItem("detective_hat", "Gorra Exploradora", WardrobeCategory.HATS, 60, "🔍", 0xFFBCAAA4, 0xFF8D6E63, "Lista para la aventura"),
        WardrobeItem("chef_hat", "Gorro de Chef", WardrobeCategory.HATS, 50, "🧑‍🍳", 0xFFFFFFFF, 0xFFB0BEC5, "Para cocinar delicias"),

        // Outfits
        WardrobeItem("mint_romper", "Mameluco Menta", WardrobeCategory.OUTFITS, 0, "🌱", 0xFFA5D6A7, 0xFF66BB6A, "Fresco y cómodo"),
        WardrobeItem("star_pajamas", "Pijama Estrellado", WardrobeCategory.OUTFITS, 40, "✨", 0xFFCE93D8, 0xFFAB47BC, "Suave para dormir"),
        WardrobeItem("striped_overalls", "Overol a Rayas", WardrobeCategory.OUTFITS, 65, "👖", 0xFF90CAF9, 0xFF1E88E5, "Clásico y divertido"),
        WardrobeItem("pink_vest", "Chaleco Rosado", WardrobeCategory.OUTFITS, 55, "🧥", 0xFFF48FB1, 0xFFEC407A, "Abrigo esponjoso"),
        WardrobeItem("fairy_cape", "Capa Mágica", WardrobeCategory.OUTFITS, 90, "🧚", 0xFFFFD180, 0xFFFFAB40, "Llena de polvitos mágicos"),

        // Accessories
        WardrobeItem("heart_glasses", "Gafas de Corazón", WardrobeCategory.ACCESSORIES, 0, "💖", 0xFFFF4081, 0xFFF50057, "Lentes con mucho amor"),
        WardrobeItem("golden_bell", "Cascabel Sonoro", WardrobeCategory.ACCESSORIES, 35, "🔔", 0xFFFFD54F, 0xFFFF8F00, "Suena con cada paso"),
        WardrobeItem("cute_pacifier", "Chupón Pastel", WardrobeCategory.ACCESSORIES, 45, "🫧", 0xFF80DEEA, 0xFF00ACC1, "Para calmar a la bebé"),
        WardrobeItem("rainbow_scarf", "Bufanda Arcoíris", WardrobeCategory.ACCESSORIES, 70, "🧣", 0xFFFF8A80, 0xFFB388FF, "Colores cálidos")
    )
}
