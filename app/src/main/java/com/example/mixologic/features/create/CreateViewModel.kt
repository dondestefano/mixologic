package com.example.mixologic.features.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.managers.FirebaseManager

enum class CreateState {
    IDLE,
    SAVING,
    SAVED,
    ERROR
}

class CreateViewModel(): ViewModel() {
    val createState = MutableLiveData<CreateState>()
    var recipe: Recipe? = null

    fun saveRecipe(recipe: Recipe) {
        updateRecipe(recipe)

        createState.value = CreateState.SAVING
        recipe.id?.let {
            FirebaseManager.getRecipeDatabase().document(recipe.id)
                .set(recipe)
                .addOnSuccessListener {
                    createState.value = CreateState.SAVED
                    Log.d("!!!", "Recipe successfully added to database")
                }
                .addOnFailureListener {
                    createState.value = CreateState.ERROR
                    Log.d("!!!", "Failed to add recipe to database")
                }
        }
    }

    fun resetViewModel() {
        recipe = null
        createState.value = CreateState.IDLE
    }

    private fun updateRecipe(updatedRecipe: Recipe) {
        recipe = updatedRecipe
    }
}