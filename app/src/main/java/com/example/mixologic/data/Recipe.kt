package com.example.mixologic.data

data class Recipe (
        val name: String?,
        val preparation: String?,
        val liquors: List<Ingredient>?,
        val ingredients: List<Ingredient>?
)