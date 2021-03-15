package com.example.mixologic.features.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mixologic.R
import com.example.mixologic.managers.AccountManager

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailLoginEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordLoginEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<TextView>(R.id.signupTextView)

        signupButton.setOnClickListener{
            goToSignup()
        }

        loginButton.setOnClickListener{
            AccountManager.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
            this
            )
        }
    }

    private fun goToSignup() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}