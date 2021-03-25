package com.example.mixologic.features.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mixologic.data.UserData
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.FirebaseManager

enum class FetchState {
    ERROR,
    LOADING,
    SUCCESS
}

class SplashViewModel: ViewModel() {
    val fetchState = MutableLiveData<FetchState>()

    fun fetchUserData(context: Context) {
        fetchState.value = FetchState.LOADING
        FirebaseManager.getUsersUserData(AccountManager.getUser().uid).document("info")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val data = value.toObject(UserData::class.java)!!
                    AccountManager.setUserData(data)
                    fetchState.value = FetchState.SUCCESS
                } else {
                    Toast.makeText(context, "Error fetching userdata: $error", Toast.LENGTH_SHORT)
                        .show()
                    fetchState.value = FetchState.ERROR
                }
            }
    }

}