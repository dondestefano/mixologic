package com.example.mixologic.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.FetchState
import com.example.mixologic.data.Ingredient
import com.example.mixologic.managers.FilterManager
import com.example.mixologic.managers.FirebaseManager

class SearchViewModel: ViewModel() {
    var recipes = listOf<Recipe>()
    val recipeState = MutableLiveData<FetchState>()
    private lateinit var filterManger: FilterManager

    fun fetchRecipes() {
        recipeState.value = FetchState.LOADING
        FirebaseManager.getRecipeDatabase().addSnapshotListener{ value, error ->
            if (value != null) {
                val allRecipes = value.toObjects(Recipe::class.java)
                filterManger = FilterManager(allRecipes)
                val dummyUserPantry = listOf(Ingredient("Cognac", 13), Ingredient("Arrack", 13))
                recipes = filterManger.filterByPantry(dummyUserPantry)
                recipeState.value = FetchState.SUCCESS
            } else {
                recipeState.value = FetchState.ERROR
            }
        }
    }
}