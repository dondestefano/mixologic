package com.example.mixologic.data

data class Recipe (
        val name: String?,
        val preparation: String?,
        val liquors: List<Liquor>?,
        val ingredients: List<Ingredient>?
)

data class Ingredient(
        val name: String?,
        val amount: Int?,
        val amountMeasurement: String?
)