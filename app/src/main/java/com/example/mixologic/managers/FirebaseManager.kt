package com.example.mixologic.managers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseManager {
    private val database = FirebaseFirestore.getInstance()

    fun getRecipeDatabase(): CollectionReference {
        return database.collection("recipes")
    }
}