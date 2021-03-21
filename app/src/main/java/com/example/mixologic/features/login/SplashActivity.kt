package com.example.mixologic.features.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mixologic.R
import com.example.mixologic.features.main.MainActivity
import com.example.mixologic.managers.AccountManager
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val notLoggedInIntent = Intent(this, LoginActivity::class.java)
        val loggedInIntent = Intent(this, MainActivity::class.java)

        if (AccountManager.getAuth().currentUser == null) {
            startActivity(notLoggedInIntent)
            finish()
        } else {
            AccountManager.setUser()
            AccountManager.setUserData(this)
            startActivity(loggedInIntent)
            finish()
        }
    }
}