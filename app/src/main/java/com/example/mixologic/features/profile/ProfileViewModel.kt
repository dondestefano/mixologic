package com.example.mixologic.features.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.application.MixologicApplication
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.FetchState
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.FirebaseManager
import java.util.*

enum class ImageState {
    UPLOADING,
    SUCCESS,
    ERROR
}

class ProfileViewModel: ViewModel() {
    var userRecipes = listOf<Recipe>()

    val recipeState = MutableLiveData<FetchState>()
    val imageState = MutableLiveData<ImageState>()

    val username = AccountManager.getUsername()
    var imageURL: Uri? = null

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
                        Log.e("Error", "Failed fetching user recipes: $error")
                        recipeState.value = FetchState.ERROR
            }
        }
    }

    fun saveProfilePicture(application: MixologicApplication) {
        imageState.value = ImageState.UPLOADING
        if (application.checkNetwork()) {
            if(imageURL != null) {
                // Save image if it has been uploaded
                val filename = UUID.randomUUID().toString()
                val imageRef = FirebaseManager.getStorage().getReference("/images/$filename")

                imageRef.putFile(imageURL!!).addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {
                        AccountManager.saveUserImage(imageURL!!)
                        imageState.value = ImageState.SUCCESS
                    }
                        .addOnFailureListener{ imageState.value = ImageState.ERROR }
                }.addOnFailureListener{ imageState.value = ImageState.ERROR }
            } else {
                // Save recipe without image
                imageState.value = ImageState.ERROR
            }
        } else {
            imageState.value = ImageState.ERROR
        }
    }
}