package com.example.mixologic.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.FetchState
import com.example.mixologic.data.Ingredient
import com.example.mixologic.managers.FilterManager
import com.example.mixologic.managers.FirebaseManager

const val SORT_FAVOURITE = "sort_favourite"
const val SORT_AZ = "sort_az"
const val SORT_ZA = "sort_za"


class SearchViewModel: ViewModel() {
    var recipes = listOf<Recipe>()
    val recipeState = MutableLiveData<FetchState>()
    private var filtered = false
    private lateinit var filterManger: FilterManager

    fun fetchRecipes() {
        recipeState.value = FetchState.LOADING
        FirebaseManager.getRecipeDatabase().addSnapshotListener{ value, error ->
            if (value != null) {
                val allRecipes = value.toObjects(Recipe::class.java)
                filterManger = FilterManager(allRecipes)
                recipes = allRecipes

                recipeState.value = FetchState.SUCCESS
            } else {
                recipeState.value = FetchState.ERROR
            }
        }
    }

    fun filterByPantry() {
        recipeState.value = FetchState.LOADING

        filtered = !filtered

        recipes = if (filtered) {
            val dummyUserPantry = listOf(Ingredient("Cognac", 13), Ingredient("Arrack", 13))
            filterManger.filterByPantry(dummyUserPantry)
        } else {
            filterManger.originalList
        }

        recipeState.value = FetchState.SUCCESS
    }

    fun sortList(sortType: String) {
        recipeState.value = FetchState.LOADING

        when(sortType) {
            SORT_FAVOURITE -> {
                recipes = recipes.sortedByDescending { recipe -> recipe.likes?.size }
            }
            SORT_AZ -> {
                recipes = recipes.sortedBy { recipe -> recipe.name }
            }
            SORT_ZA -> {
                recipes = recipes.sortedByDescending { recipe -> recipe.name }
            }
        }

        recipeState.value = FetchState.SUCCESS
    }
}