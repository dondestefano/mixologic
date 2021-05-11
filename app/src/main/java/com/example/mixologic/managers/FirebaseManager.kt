package com.example.mixologic.managers

import com.example.mixologic.data.Like
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

object FirebaseManager {
    private val database = FirebaseFirestore.getInstance()
    private val cloudStorage = FirebaseStorage.getInstance()

    fun getRecipeDatabase(): CollectionReference {
        return database.collection("recipes")
    }

    fun getRecipe(recipeId: String): DocumentReference {
        return getRecipeDatabase().document(recipeId)
    }

    fun getUserDatabase(): CollectionReference {
        return database.collection("users")
    }

    fun getUsersUserData(uid: String): CollectionReference {
        return getUserDatabase()
                .document(uid)
                .collection("userData")
    }

    fun getUserPantry(uid: String): CollectionReference {
        return getUserDatabase()
            .document(uid)
            .collection("pantry")
    }

    fun getUsersRecipes(userId: String): Query {
        return getRecipeDatabase().whereEqualTo("creatorId", userId)
    }

    fun getLiquorDatabase(): CollectionReference {
        return database.collection("liquors")
    }

    fun getLikedRecipes(): Query {
        return getRecipeDatabase().whereArrayContains("likes", Like(AccountManager.getUser().uid))
    }

    fun getStorage(): FirebaseStorage {
        return cloudStorage
    }
}