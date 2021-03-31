package com.example.mixologic.managers

import com.example.mixologic.data.Ingredient

object LiquorManager {
    private var liquors = mutableListOf<String>()

    fun fetchLiquors() {
        liquors.clear()
        FirebaseManager.getLiquorDatabase().addSnapshotListener{ value, error ->
            value?.forEachIndexed { _, item ->
                val liquor = item.toObject(Ingredient::class.java)
                liquor.name?.let {
                    liquors.add(it)
                }
            }
        }
    }

    fun getLiquors(): List<String> {
        return liquors
    }
}