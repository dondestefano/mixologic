package com.example.mixologic.managers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseManager {
    private val database = FirebaseFirestore.getInstance()

    fun getRecipeDatabase(): CollectionReference {
        return database.collection("recipes")
    }

    fun getUserDatabase(): CollectionReference {
        return database.collection("users")
    }

    fun getUsersRecipes(): CollectionReference {
        return getUserDatabase()
                .document(AccountManager.getUser().uid)
                .collection("recipes")
    }
}