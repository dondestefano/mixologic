package com.example.mixologic.managers

import com.example.mixologic.data.Ingredient
import com.example.mixologic.data.Recipe

class FilterManager(private val originalList: List<Recipe>) {
    fun filterByPantry(userPantry: List<Ingredient>): List<Recipe> {

        val filterOutNotPantry = { recipe: Recipe ->
            userHasIngredient(recipe, userPantry)
        }

        return originalList.filter(filterOutNotPantry)
    }

    private fun userHasIngredient(recipe: Recipe, userPantry: List<Ingredient>): Boolean {
        var userHasIngredient = true
        if (recipe.liquors.isNullOrEmpty()) {
            userHasIngredient = true
        } else {
            recipe.liquors.forEachIndexed { _, liquor ->
                userPantry.forEachIndexed lit@{ _, userLiquor ->
                    if (liquor.name == userLiquor.name) {
                        userHasIngredient = true
                        return@lit
                    } else {
                        userHasIngredient = false
                    }
                }
            }
        }

        return userHasIngredient
    }
}