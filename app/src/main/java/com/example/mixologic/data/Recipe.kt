package com.example.mixologic.data

data class Recipe (
        val name: String?,
        val preparation: List<PreparationStep>,
        val liquors: List<Liquor>,
        val ingredients: List<Ingredient>
)

data class PreparationStep(
        val instruction: String?,
        val step: Int?,
)

data class Ingredient(
        val name: String?,
        val amount: Int?,
        val amountMeasurement: String?
)