package com.example.mixologic.features.create

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.managers.FirebaseManager
import com.example.mixologic.managers.LiquorManager

class CreateViewModel(): ViewModel() {
    val liquor = LiquorManager.getLiquors()
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
        }
    }
}