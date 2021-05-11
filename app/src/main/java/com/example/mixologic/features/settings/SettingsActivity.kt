package com.example.mixologic.features.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mixologic.R
import com.example.mixologic.managers.AccountManager

class SettingsActivity : AppCompatActivity() {
    private lateinit var logoutButton: TextView
    private lateinit var backButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        logoutButton = findViewById(R.id.logoutButton)
        backButton = findViewById(R.id.backButton)

        logoutButton.setOnClickListener{
            logout()
        }

        backButton.setOnClickListener{
            onBackPressed()
        }
    }

    private fun logout() {
        AccountManager.getAuth().signOut()
        setResult(66)
        finish()
    }
}