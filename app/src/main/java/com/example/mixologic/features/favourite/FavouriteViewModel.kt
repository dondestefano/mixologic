package com.example.mixologic.features.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.RecipeState
import com.example.mixologic.managers.FirebaseManager

class FavouriteViewModel: ViewModel() {
    var recipes = listOf<Recipe>()
    val recipeState = MutableLiveData<RecipeState>()

    fun fetchLikedRecipes() {
        recipeState.value = RecipeState.LOADING
        FirebaseManager.getLikedRecipes().addSnapshotListener{ value, error ->
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