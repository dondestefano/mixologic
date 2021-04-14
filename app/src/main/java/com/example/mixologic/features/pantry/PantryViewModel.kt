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
    var pantryList = LiquorManager.getPantry()
    var remainingList = mutableListOf<String>()
    val pantryState = MutableLiveData<FetchState>()

    fun savePantryItem(item: Ingredient) {
        if (item.name != null) {
            FirebaseManager.getUserPantry(AccountManager.getUser().uid).document(item.name)
                .set(item)
                .addOnSuccessListener {
                    updatePantryList()
                    Log.d("!!!", "Item successfully added to database")
                    pantryState.value = FetchState.SUCCESS
                }
                .addOnFailureListener {
                    Log.d("!!!", "Failed to add item to database")
                    pantryState.value = FetchState.ERROR
                }
        }
    }

    fun deletePantryItem(item: Ingredient) {
        if (item.name != null) {
            FirebaseManager.getUserPantry(AccountManager.getUser().uid).document(item.name)
                .delete()
                .addOnSuccessListener {
                    updatePantryList()
                    Log.d("!!!", "Item successfully deleted")
                    pantryState.value = FetchState.SUCCESS
                }
                .addOnFailureListener {
                    Log.d("!!!", "Failed to delete item to database")
                    pantryState.value = FetchState.ERROR
                }
        }
    }

    fun updatePantryList() {
        pantryState.value = FetchState.LOADING

        pantryList = LiquorManager.getPantry()
        calculateRemaining()

        pantryState.value = FetchState.SUCCESS
    }

    fun calculateRemaining() {
        remainingList.clear()
        remainingList.addAll(LiquorManager.getLiquors())

        pantryList.forEachIndexed { _, ingredient ->
            remainingList.remove(ingredient.name)
        }
    }
}