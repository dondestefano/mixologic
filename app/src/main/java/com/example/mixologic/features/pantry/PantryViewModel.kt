package com.example.mixologic.features.pantry

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Ingredient
import com.example.mixologic.data.FetchState
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.FirebaseManager
import com.example.mixologic.managers.LiquorManager

class PantryViewModel(): ViewModel() {
    var pantryList = listOf<Ingredient>()
    var remainingList = mutableListOf<String>()
    val pantryState = MutableLiveData<FetchState>()

    fun savePantryItem(item: Ingredient) {
        if (item.name != null) {
            FirebaseManager.getUserPantry(AccountManager.getUser().uid).document(item.name)
                .set(item)
                .addOnSuccessListener {
                    calculateRemaining()
                    Log.d("!!!", "Item successfully added to database")
                }
                .addOnFailureListener {
                    Log.d("!!!", "Failed to add item to database")
                }
        }
    }

    fun deletePantryItem(item: Ingredient) {
        if (item.name != null) {
            FirebaseManager.getUserPantry(AccountManager.getUser().uid).document(item.name)
                .delete()
                .addOnSuccessListener {
                    calculateRemaining()
                    Log.d("!!!", "Item successfully deleted")
                }
                .addOnFailureListener {
                    Log.d("!!!", "Failed to delete item to database")
                }
        }
    }

    fun fetchPantry() {
        pantryState.value = FetchState.LOADING
        FirebaseManager.getUserPantry(AccountManager.getUser().uid).addSnapshotListener{ value, error ->
            if (value != null) {
                val userPantry = value.toObjects(Ingredient::class.java)
                pantryList = userPantry

                calculateRemaining()

                pantryState.value = FetchState.SUCCESS
            } else {
                pantryState.value = FetchState.ERROR
            }
        }
    }

    private fun calculateRemaining() {
        remainingList.clear()
        remainingList.addAll(LiquorManager.getLiquors())

        pantryList.forEachIndexed { _, ingredient ->
            remainingList.remove(ingredient.name)
        }
    }
}