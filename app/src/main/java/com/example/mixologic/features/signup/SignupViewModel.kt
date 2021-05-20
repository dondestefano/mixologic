package com.example.mixologic.features.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.UserData
import com.example.mixologic.managers.AccountManager

enum class SignupState {
    ERROR,
    SUCCESS,
    NO_USERNAME
}

class SignupViewModel: ViewModel() {
    val signupState = MutableLiveData<SignupState>()

    fun signUp(username: String, email: String, password: String) {
        if (username == "") {
            signupState.value = SignupState.NO_USERNAME
        } else {
            AccountManager.getAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            AccountManager.setUser()

                            val userData = UserData(username, email, null)
                            AccountManager.saveUserData(userData)

                            signupState.value = SignupState.SUCCESS
                        } else {
                            signupState.value = SignupState.ERROR
                        }
                    }
        }
    }
}