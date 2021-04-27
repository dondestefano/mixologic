package com.example.mixologic.features.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.application.MixologicApplication
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.FetchState
import com.example.mixologic.managers.FirebaseManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavouriteViewModel: ViewModel() {
    var recipes = listOf<Recipe>()
    val recipeState = MutableLiveData<FetchState>()

    fun fetchLikedRecipes(application: MixologicApplication) {
        recipeState.value = FetchState.LOADING
        if(application.checkNetwork()) {
            FirebaseManager.getLikedRecipes().addSnapshotListener{ value, error ->
                if (value != null) {
                    val allRecipes = value.toObjects(Recipe::class.java)
                    recipes = allRecipes
                    recipeState.value = FetchState.SUCCESS

                    saveToCache(application)
                } else {
                    recipeState.value = FetchState.ERROR
                }
            }
        } else {
            fetchFromCached(application)
        }

    }

    fun fetchFromCached(application: MixologicApplication) {
        GlobalScope.launch {
            application.favouriteRepository.getCachedFavourites()
        }

        recipes = application.favouriteRepository.favourites
        recipeState.value = FetchState.SUCCESS
    }

    fun saveToCache(application: MixologicApplication) {
        GlobalScope.launch {
            application.favouriteRepository.saveAllToCache(recipes)
        }
    }
}