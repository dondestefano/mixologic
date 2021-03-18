package com.example.mixologic.features.create

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.managers.FirebaseManager

class CreateViewModel(): ViewModel() {
    val liquor = listOf("Vodka", "Rum", "Gin", "Whiskey")
    val unit = listOf("ml", "cl", "dl", "st")

    fun saveRecipe(recipe: Recipe) {
        recipe.id?.let {
            FirebaseManager.getRecipeDatabase().document(recipe.id)
                .set(recipe)
                .addOnSuccessListener {
                    Log.d("!!!", "Recipe successfully added to database")
                }
                .addOnFailureListener {
                    Log.d("!!!", "Failed to add recipe to database")
                }

            FirebaseManager.getUsersRecipes().document(recipe.id)
                    .set(recipe)
                    .addOnSuccessListener {
                        Log.d("!!!", "Recipe successfully added to users collection")
                    }
                    .addOnFailureListener {
                        Log.d("!!!", "Failed to add recipe to users collection")
                    }
        }
    }
}