package com.example.mixologic

import com.example.mixologic.data.Ingredient
import com.example.mixologic.data.Like
import com.example.mixologic.data.Recipe
import com.example.mixologic.managers.FilterManager
import org.junit.Test
import org.junit.Assert.*

class FilterManagerTest {
    private val recipe1 = Recipe(
            "1",
            "Drink 1",
            "Mix",
            listOf(Ingredient("Vodka", 2, "cl")),
            listOf(Ingredient("Orange juice",160, "ml")),
            "User",
            null,
            mutableListOf(
                    Like("User1"),
                    Like("User2")
            )
    )

    private val recipe2 = Recipe(
            "2",
            "Drink 2",
            "Shake",
            listOf(
                    Ingredient("Whiskey", 15, "ml"),
                    Ingredient("Rum", 16, "ml")
            ),
            listOf(Ingredient("Orange juice",330, "ml")),
            "User",
            null,
            mutableListOf()
    )

    private val recipe3 = Recipe(
            "3",
            "Drink 3",
            "Stir",
            listOf(
                    Ingredient("Vodka", 1, "cl"),
                    Ingredient("Rum", 16, "dl"),
                    Ingredient("Aquavit", 24, "ml")
            ),
            listOf(Ingredient("Sprite", 33, "cl")),
            "User",
            null,
            mutableListOf(
                    Like("User1"),
                    Like("User2"),
                    Like("User3")
            )
    )

    private val vodka = Ingredient("Vodka", 10, "ml")
    private val rum = Ingredient("Rum", 1800, "ml")
    private val aquavit = Ingredient("Aquavit", 24, "ml")

    private val list = listOf(recipe1, recipe2, recipe3)
    private val pantry = listOf(vodka, rum, aquavit)
    private val filterManager = FilterManager(list)

    @Test
    fun test_filterByKeyword() {
        val filteredByDrink = filterManager.filterByKeyword("Dri")
        assertEquals(filteredByDrink, list)

        val filteredByNumber = filterManager.filterByKeyword("2")
        assertEquals(filteredByNumber, listOf(recipe2))
    }

    @Test
    fun test_filterByPantry() {
        val filteredByPantry = filterManager.filterByPantry(pantry)
        assertEquals(filteredByPantry, listOf(recipe3))
    }
}