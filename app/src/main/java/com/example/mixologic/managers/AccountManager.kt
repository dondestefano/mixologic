package com.example.mixologic.managers

import android.content.Context
import android.widget.Toast
import com.example.mixologic.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AccountManager {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: FirebaseUser

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun setUser() {
        auth.currentUser.let {
            user = it!!
        }
    }

    fun getUser(): FirebaseUser {
        return user
    }

    fun signUp(username: String, email: String, password: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
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