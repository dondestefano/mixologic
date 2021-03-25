package com.example.mixologic.managers

import com.example.mixologic.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AccountManager {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: FirebaseUser
    private lateinit var userData: UserData

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun setUser() {
        auth.currentUser.let {
            user = it!!
        }
    }

    fun setUserData(data: UserData) {
        userData = data
    }

    fun getUser(): FirebaseUser {
        return user
    }

    fun getUsername(): String? {
        return userData.name
    }

    fun getUserdata(): UserData {
        return userData
    }

    fun saveUserData(userData: UserData) {
        FirebaseManager.getUsersUserData(user.uid)
                .document("info")
                .set(userData)
    }
}