package com.example.mixologic.features.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.mixologic.R
import com.example.mixologic.features.main.MainActivity
import com.example.mixologic.managers.AccountManager
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    private lateinit var notLoggedInIntent: Intent
    private lateinit var loggedInIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loggedInIntent = Intent(this, MainActivity::class.java)
        notLoggedInIntent = Intent(this, LoginActivity::class.java)

        observeViewModel()

        if (AccountManager.getAuth().currentUser == null) {
            startActivity(notLoggedInIntent)
            finish()
        } else {
            AccountManager.setUser()
            splashViewModel.fetchUserData(this)
        }
    }

    private fun observeViewModel() {
        splashViewModel.fetchState.observe(this, Observer {
            when (it) {
                FetchState.SUCCESS -> {
                    startActivity(loggedInIntent)
                    finish()
                }
                FetchState.ERROR -> {
                    AccountManager.getAuth().signOut()
                    startActivity(notLoggedInIntent)
                    finish()
                }
                else -> {}
            }
        })
    }
}