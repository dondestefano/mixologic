package com.example.mixologic.managers

import android.content.Context
import android.widget.Toast
import com.example.mixologic.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User

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

    fun getUserdata(): UserData {
        return userData
    }

    fun getUsername(): String? {
        return userData.name
    }

    fun signUp(username: String, email: String, password: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setUser()
                    val userData = UserData(username, email, null)
                    saveUserData(userData)

                    Toast.makeText(context, "User signed up.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "E-mail or username already taken.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun saveUserData(userData: UserData) {
        FirebaseManager.getUsersUserData(user.uid)
                .document("info")
                .set(userData)
    }
}