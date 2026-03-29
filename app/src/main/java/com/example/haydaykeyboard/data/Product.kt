package com.example.haydaykeyboard.data

data class Product(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val level: Int,
    val machineId: String,
    val machineNameEn: String,
    val machineNameAr: String,
    val imageUrl: String? = null,
) {
    val localImageCandidates: List<String>
        get() = listOf(
            "product_images/$id.png",
            "product_images/$id.webp",
            "product_images/$id.jpg",
            "product_images/$id.jpeg",
        )
}
