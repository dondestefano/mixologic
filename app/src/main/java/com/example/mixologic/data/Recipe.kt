package com.example.mixologic.data

data class Recipe (
        val name: String? = null,
        val preparation: String? = null,
        val liquors: List<Ingredient>? = null,
        val ingredients: List<Ingredient>? = null,
        val id: String? = null,
        val creatorId: String? = null,
        val imageURL: String? = null,
)