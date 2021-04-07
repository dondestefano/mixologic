package com.example.mixologic.features.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.FetchState
import com.example.mixologic.managers.FirebaseManager

class FavouriteViewModel: ViewModel() {
    var recipes = listOf<Recipe>()
    val recipeState = MutableLiveData<FetchState>()

    fun fetchLikedRecipes() {
        recipeState.value = FetchState.LOADING
        FirebaseManager.getLikedRecipes().addSnapshotListener{ value, error ->
            if (value != null) {
                val allRecipes = value.toObjects(Recipe::class.java)
                recipes = allRecipes
                recipeState.value = FetchState.SUCCESS
            } else {
                recipeState.value = FetchState.ERROR
            }
        }
    }
}