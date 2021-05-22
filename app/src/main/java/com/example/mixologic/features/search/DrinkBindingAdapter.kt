package com.example.mixologic.features.search

import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.mixologic.R
import com.example.mixologic.application.MixologicApplication
import com.example.mixologic.data.*
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.EncryptionManager
import com.example.mixologic.managers.FirebaseManager
import com.example.mixologic.managers.LikeManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Picasso
            .get()
            .load(imageUrl)
            .fit()
            .centerCrop()
            .into(view)
    } else {
        val drinkIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_drink)
        drinkIcon!!.setTint(ContextCompat.getColor(view.context, R.color.white))
        view.setImageDrawable(drinkIcon)
    }
}

@BindingAdapter("imageCircleFromUrl")
fun bindCircleImageFromUrl(view: CircleImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Picasso
            .get()
            .load(imageUrl)
            .fit()
            .centerCrop()
            .into(view)
    } else {
        val profileIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_profile)
        view.setImageDrawable(profileIcon)
    }
}

@BindingAdapter("list", "liquor")
fun appendIngredientsFromList(view: TextView, list: List<Ingredient>, liquor: Boolean ) {
    view.text = if (liquor) {
        "Liquors: "
    } else {
        "Ingredients: "
    }

    list.forEachIndexed{ index, ingredient ->
        if (index == list.size-1) {
            view.append(ingredient.name)
        } else {
            view.append(ingredient.name + ", ")
        }
    }
}

@BindingAdapter("getUserName")
fun getUserName(view: TextView, id: String?) {
    if (id != null) {
        val application = view.context.applicationContext as MixologicApplication
        val cachedData = application.dataRepository.getData(id)

        if (id == AccountManager.getUser().uid) {
            view.text = AccountManager.getUsername()
        } else {
            if (cachedData != null) {
                view.text = decryptName(cachedData)
            } else {
                FirebaseManager.getUsersUserData(id)
                        .document("info")
                        .addSnapshotListener { value, error ->
                            if (value != null) {
                                val userData = value.toObject(UserData::class.java) ?: AccountManager.getDefaultData()
                                view.text = userData.name

                                val encryption = userData.name?.toByteArray()?.let { EncryptionManager.encrypt(it, "hej".toCharArray()) }
                                val dataToCache = CachedData(
                                    id,
                                    Base64.encodeToString(encryption?.get("encrypted"), Base64.NO_WRAP),
                                    userData.profileImageURL,
                                    Base64.encodeToString(encryption?.get("salt"), Base64.NO_WRAP),
                                    Base64.encodeToString(encryption?.get("iv"), Base64.NO_WRAP),
                                )

                                GlobalScope.launch {
                                    Log.d("!!!", "Saving")
                                    application.dataRepository.saveDataToCache(dataToCache)
                                }
                            }
                        }
            }
        }
    }
}

@BindingAdapter("getUserProfileImage")
fun getUserProfileImage(view: CircleImageView, id: String?) {
    if (id != null) {
        val application = view.context.applicationContext as MixologicApplication
        val cachedData = application.dataRepository.getData(id)

        if (id == AccountManager.getUser().uid) {
            bindCircleImageFromUrl(view, AccountManager.getUserdata().profileImageURL)
        } else {
            if (cachedData != null) {
                bindCircleImageFromUrl(view, cachedData.profileImageURL)
            } else {
                FirebaseManager.getUsersUserData(id)
                        .document("info")
                        .addSnapshotListener { value, error ->
                            if (value != null) {
                                val userData = value.toObject(UserData::class.java) ?: AccountManager.getDefaultData()
                                bindCircleImageFromUrl(view, userData.profileImageURL)

                                val encryption = userData.name?.toByteArray()?.let { EncryptionManager.encrypt(it, "hej".toCharArray()) }
                                val dataToCache = CachedData(
                                    id,
                                    Base64.encodeToString(encryption?.get("encrypted"), Base64.NO_WRAP),
                                    userData.profileImageURL,
                                    Base64.encodeToString(encryption?.get("salt"), Base64.NO_WRAP),
                                    Base64.encodeToString(encryption?.get("iv"), Base64.NO_WRAP),
                                )

                                GlobalScope.launch {
                                    application.dataRepository.saveDataToCache(dataToCache)
                                }
                            }
                        }
            }
        }
    }
}

fun decryptName(cachedData: CachedData): String {
    val encrypted = Base64.decode(cachedData.profileName, Base64.NO_WRAP)
    val salt = Base64.decode(cachedData.salt, Base64.NO_WRAP)

    val iv = Base64.decode(cachedData.iv, Base64.NO_WRAP)

    val decrypted = EncryptionManager.decrypt(
            hashMapOf("iv" to iv, "salt" to salt, "encrypted" to encrypted), "hej".toCharArray())

    return decrypted?.let { String(it, Charsets.UTF_8) } ?: "Error"
}

@BindingAdapter("getLikes")
fun getLikes(view: TextView, recipe: Recipe) {
    if (!recipe.likes.isNullOrEmpty()) {
        view.text = recipe.likes!!.size.toString()
    } else {
        view.text = "0"
    }
}

@BindingAdapter("likeStatus")
fun likeStatus(view: ImageView, recipe: Recipe) {
    if (recipe.likes.isNullOrEmpty()) {
        val notLikedIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_heart_outline)
        view.setImageDrawable(notLikedIcon)
    }

    else {
        recipe.likes?.let { it ->
            if (LikeManager.hasUserLiked(it, AccountManager.getUser().uid)) {
                val likedIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_heart_full)
                view.setImageDrawable(likedIcon)
            } else {
                val notLikedIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_heart_outline)
                view.setImageDrawable(notLikedIcon)
            }
        }
    }
}