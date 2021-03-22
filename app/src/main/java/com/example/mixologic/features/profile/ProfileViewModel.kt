package com.example.mixologic.features.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.UserData
import com.example.mixologic.features.login.LoginState
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.FirebaseManager

enum class RecipeState {
    ERROR,
    LOADING,
    SUCCESS
}

class ProfileViewModel: ViewModel() {
    var userRecipes = listOf<Recipe>()
    val recipeState = MutableLiveData<RecipeState>()
    val username = AccountManager.getUsername()

    fun fetchUsersRecipes() {
        recipeState.value = RecipeState.LOADING
        FirebaseManager.getUsersRecipes().addSnapshotListener{value, error ->
            if (value != null) {
                val recipes = value.toObjects(Recipe::class.java)
                userRecipes = recipes
                recipeState.value = RecipeState.SUCCESS
            } else {
                recipeState.value = RecipeState.ERROR
            }
        }
    }
}