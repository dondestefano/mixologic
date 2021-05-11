package com.example.mixologic.application

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mixologic.data.room.DataRepository
import com.example.mixologic.data.room.FavouriteRepository
import com.example.mixologic.data.room.RecipeCacheDatabase
import com.example.mixologic.managers.LikeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MixologicApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { RecipeCacheDatabase.getInstance(this, applicationScope) }

    val favouriteRepository by lazy { FavouriteRepository(database.recipeCacheDao) }
    val dataRepository by lazy { DataRepository(database.dataCacheDao) }

    private var isNetworkConnected = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        registerNetworkCallback(applicationContext)

        LikeManager.application = this
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
            })
        } catch (e: Exception) {
            isNetworkConnected = false
        }
    }
}