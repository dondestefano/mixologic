package com.example.mixologic.managers

import android.util.Log
import com.example.mixologic.data.Ingredient
import com.example.mixologic.data.Recipe

class FilterManager(val originalList: List<Recipe>) {
    fun filterByPantry(userPantry: List<Ingredient>): List<Recipe> {
        val filterOutNotPantry = { recipe: Recipe ->
            userHasIngredient(recipe, userPantry)
        }

        return originalList.filter(filterOutNotPantry)
    }

    fun filterByKeyword(keyword: String): List<Recipe> {
        val filterBySearch = { recipe: Recipe ->
            recipe.name?.contains(keyword)!!
        }

        return originalList.filter(filterBySearch)
    }

    private fun userHasIngredient(recipe: Recipe, userPantry: List<Ingredient>): Boolean {
        var userHasIngredient = false

        if (recipe.liquors.isNullOrEmpty()) {
            userHasIngredient = true
        } else {
            recipe.liquors.forEachIndexed lit@{ index, liquor ->
                if (index > 0 && !userHasIngredient) {
                    return userHasIngredient
                }

                userPantry.forEach { userLiquor ->
                    if (liquor.name == userLiquor.name && convertedAmount(liquor, userLiquor.unit!!)!! <= userLiquor.amount!!) {
                        Log.d("!!!", "${userLiquor.name} was ${liquor.name} which is ${index+1} of ${recipe.liquors.size} for ${recipe.name}")
                        userHasIngredient = true
                        return@lit
                    } else {
                        Log.e("!!!", "${userLiquor.name} was not have ${liquor.name} which is ${index+1} of ${recipe.liquors.size} for ${recipe.name} ")
                        userHasIngredient = false
                    }
                }
            }
        }

        return userHasIngredient
    }

    private fun convertedAmount(liquor: Ingredient, unit: String): Int? {
        if (liquor.unit == unit) {
            return liquor.amount
        } else {
            if(unit == "ml") {
                if (liquor.unit == "cl") {
                    return liquor.amount?.times(10)
                }
                else if (liquor.unit == "dl") {
                    return liquor.amount?.times(100)
                }
            }
            else if(unit == "cl") {
                if (liquor.unit == "ml") {
                    return liquor.amount?.div(10)
                }
                else if (liquor.unit == "dl") {
                    return liquor.amount?.times(10)
                }
            }
            else if(unit == "dl") {
                if (liquor.unit == "ml") {
                    return liquor.amount?.times(100)
                }
                else if (liquor.unit == "dl") {
                    return liquor.amount?.times(10)
                }
            }
        }
        return 0
    }
}