package com.example.mixologic.features.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.UserData
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.FirebaseManager
import com.example.mixologic.managers.LiquorManager

enum class LoginState {
    ERROR,
    SUCCESS
}

class LoginViewModel: ViewModel() {
    val loginState = MutableLiveData<LoginState>()

    fun login(email: String, password: String, context: Context) {
        AccountManager.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AccountManager.setUser()
                        fetchUserData(context)
                    } else {
                        task.exception
                        loginState.value = LoginState.ERROR
                    }
                }
    }

    private fun fetchUserData(context: Context) {
        FirebaseManager.getUsersUserData(AccountManager.getUser().uid).document("info")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val data = value.toObject(UserData::class.java)!!
                    AccountManager.setUserData(data)
                    LiquorManager.fetchLiquors()
                    LiquorManager.fetchUnits()
                    LiquorManager.fetchPantry()
                    loginState.value = LoginState.SUCCESS
                } else {
                    Toast.makeText(context, "Error fetching userdata: $error", Toast.LENGTH_SHORT)
                        .show()
                    loginState.value = LoginState.ERROR
                }
            }
    }
}