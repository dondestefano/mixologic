package com.example.mixologic.features.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.Recipe
import com.example.mixologic.managers.AccountManager

class ProfileViewModel: ViewModel() {
    val userRecipes = MutableLiveData<List<Recipe>>()
    val username = AccountManager.getUsername()
}