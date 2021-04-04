package com.example.mixologic.managers

import com.example.mixologic.data.Ingredient

object LiquorManager {
    private var liquors = mutableListOf<String>()
    private var units = listOf("ml", "cl", "dl", "st")

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

    fun getUnits(): List<String> {
        return units
    }
}