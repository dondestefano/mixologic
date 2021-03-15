package com.example.mixologic.managers

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AccountManager {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: FirebaseUser

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser.let {
                        user = it
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please enter your e-mail and password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun signUp(email: String, password: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "User signed up.", Toast.LENGTH_SHORT)
                        .show()
                    login(email, password, context)
                } else {
                    Toast.makeText(context, "E-mail or username already taken.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}