package com.example.mixologic.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.managers.AccountManager

enum class LoginState {
    ERROR,
    SUCCESS
}

class LoginViewModel: ViewModel() {
    val loginState = MutableLiveData<LoginState>()

    fun login(email: String, password: String) {
        AccountManager.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AccountManager.setUser()
                        loginState.value = LoginState.SUCCESS
                    } else {
                        loginState.value = LoginState.ERROR
                    }
                }
    }
}