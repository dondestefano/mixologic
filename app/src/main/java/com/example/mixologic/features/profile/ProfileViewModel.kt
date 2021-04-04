package com.example.mixologic.features.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.FetchState
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.FirebaseManager

class ProfileViewModel: ViewModel() {
    var userRecipes = listOf<Recipe>()
    val recipeState = MutableLiveData<FetchState>()
    val username = AccountManager.getUsername()

    fun fetchUsersRecipes() {
        recipeState.value = FetchState.LOADING
        FirebaseManager
                .getUsersRecipes(AccountManager.getUser().uid)
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        val recipes = value.toObjects(Recipe::class.java)
                        userRecipes = recipes
                        recipeState.value = FetchState.SUCCESS
                    } else {
                        recipeState.value = FetchState.ERROR
            }
        }
    }
}