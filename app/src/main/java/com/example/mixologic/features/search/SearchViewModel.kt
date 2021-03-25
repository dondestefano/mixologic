package com.example.mixologic.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.RecipeState
import com.example.mixologic.managers.FirebaseManager

class SearchViewModel: ViewModel() {
    var recipes = listOf<Recipe>()
    val recipeState = MutableLiveData<RecipeState>()

    fun fetchRecipes() {
        recipeState.value = RecipeState.LOADING
        FirebaseManager.getRecipeDatabase().addSnapshotListener{ value, error ->
            if (value != null) {
                val allRecipes = value.toObjects(Recipe::class.java)
                recipes = allRecipes
                recipeState.value = RecipeState.SUCCESS
            } else {
                recipeState.value = RecipeState.ERROR
            }
        }
    }
}