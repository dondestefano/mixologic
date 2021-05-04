package com.example.mixologic.managers

import android.net.Uri
import com.example.mixologic.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

object AccountManager {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: FirebaseUser
    private lateinit var userData: UserData

    private val defaultData = UserData("Error fetching data")

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun setUser() {
        auth.currentUser.let {
            user = it!!
        }
    }

    fun setUserData(data: UserData) {
        userData = data
    }

    fun getUser(): FirebaseUser {
        return user
    }

    fun getUsername(): String? {
        return userData.name
    }

    fun getUserdata(): UserData {
        return userData
    }

    fun saveUserData(userData: UserData) {
        FirebaseManager.getUsersUserData(user.uid)
                .document("info")
                .set(userData)
    }

    fun saveUserImage(selectedPhotoUri: Uri) {
        val filename = UUID.randomUUID().toString()
        // Save the image to FirebaseStorage.
        val imageRef = FirebaseManager.getStorage().getReference("/images/$filename")
        imageRef.putFile(selectedPhotoUri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                FirebaseManager.getUsersUserData(user.uid)
                        .document("info")
                        .update("profileImageURL", it.toString())
            }
        }
    }

    fun getDefaultData(): UserData {
        return defaultData
    }
}