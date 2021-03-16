package com.example.mixologic.features.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.mixologic.R
import com.example.mixologic.features.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailLoginEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordLoginEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<TextView>(R.id.signupTextView)

        observeViewModel()

        signupButton.setOnClickListener{
            startSignupActivity()
        }

        loginButton.setOnClickListener{
            loginViewModel.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
            )
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginState.observe(this, Observer {
            when (it) {
                LoginState.SUCCESS -> {
                    finish()
                    startMainActivity()
                }

                LoginState.ERROR -> {
                    Toast.makeText(
                            this,
                            "Please enter your e-mail and password.",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        })
    }

    private fun startSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}