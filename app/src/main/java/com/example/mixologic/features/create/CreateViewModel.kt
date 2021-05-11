package com.example.mixologic.features.create

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.application.MixologicApplication
import com.example.mixologic.data.Recipe
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.FirebaseManager
import java.util.*

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

    var photoUri : Uri? = null

    fun saveRecipe(recipe: Recipe, application: MixologicApplication) {
        createState.value = CreateState.SAVING
        updateRecipe(recipe)

        if (application.checkNetwork()) {
            if(photoUri != null) {
                // Save image if it has been uploaded
                val filename = UUID.randomUUID().toString()
                val imageRef = FirebaseManager.getStorage().getReference("/images/$filename")

                imageRef.putFile(photoUri!!).addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {
                            recipe.imageURL = it.toString()
                            uploadRecipeToFirebase(recipe)
                        }
                        .addOnFailureListener{ createState.value = CreateState.ERROR }
                }.addOnFailureListener{ createState.value = CreateState.ERROR }
            } else {
                // Save recipe without image
                uploadRecipeToFirebase(recipe)
            }
        } else {
            createState.value = CreateState.NO_NETWORK
        }
    }

    private fun uploadRecipeToFirebase(recipe: Recipe) {
        recipe.id.let {
            FirebaseManager.getRecipeDatabase().document(recipe.id)
                .set(recipe)
                .addOnSuccessListener {
                    createState.value = CreateState.SAVED
                }
                .addOnFailureListener {
                    createState.value = CreateState.ERROR
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