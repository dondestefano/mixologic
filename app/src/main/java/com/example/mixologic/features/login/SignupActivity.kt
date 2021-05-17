package com.example.mixologic.features.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.mixologic.R

class SignupActivity : AppCompatActivity() {
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val signupButton = findViewById<Button>(R.id.signupButton)

        observeViewModel()

        signupButton.setOnClickListener{
            signupViewModel.signUp(
                    usernameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
            )
        }
    }

    private fun observeViewModel() {
        signupViewModel.signupState.observe(this, Observer {
            when (it) {
                SignupState.SUCCESS -> {
                    Toast.makeText(this, "User signed up.", Toast.LENGTH_SHORT)
                            .show()
                    finish()
                }

                SignupState.NO_USERNAME -> {
                    Toast.makeText(this, "Please enter a username.", Toast.LENGTH_SHORT)
                            .show()
                }

                else -> {
                    Toast.makeText(this, "E-mail or username not valid.", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_right)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.slide_right)
    }
}