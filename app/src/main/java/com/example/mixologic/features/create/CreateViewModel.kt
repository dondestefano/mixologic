package com.example.mixologic.features.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.application.MixologicApplication
import com.example.mixologic.data.Recipe
import com.example.mixologic.managers.FirebaseManager

enum class CreateState {
    IDLE,
    SAVING,
    SAVED,
    ERROR,
    NO_NETWORK
}

class CreateViewModel(): ViewModel() {
    val createState = MutableLiveData<CreateState>()
    var recipe: Recipe? = null

    fun saveRecipe(recipe: Recipe, application: MixologicApplication) {
        updateRecipe(recipe)

        createState.value = CreateState.SAVING
        if (application.checkNetwork()) {
            recipe.id?.let {
                FirebaseManager.getRecipeDatabase().document(recipe.id)
                        .set(recipe)
                        .addOnSuccessListener {
                            createState.value = CreateState.SAVED
                        }
                        .addOnFailureListener {
                            createState.value = CreateState.ERROR
                        }
            }
        } else {
            createState.value = CreateState.NO_NETWORK
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