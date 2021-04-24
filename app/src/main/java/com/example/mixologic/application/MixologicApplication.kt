package com.example.mixologic.application

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mixologic.data.room.RecipeCacheDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MixologicApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { RecipeCacheDatabase.getInstance(this, applicationScope) }
    private var isNetworkConnected = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        registerNetworkCallback(applicationContext)
    }


    fun checkNetwork(): Boolean {
        return isNetworkConnected
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun registerNetworkCallback(context: Context) {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isNetworkConnected = true
                }

                override fun onLost(network: Network) {
                    isNetworkConnected = false
                }
            }
            )
            isNetworkConnected = false
        } catch (e: Exception) {
            isNetworkConnected = false
        }
    }
}