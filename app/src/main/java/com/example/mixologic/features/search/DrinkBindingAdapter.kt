package com.example.mixologic.features.search

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.mixologic.R
import com.example.mixologic.application.MixologicApplication
import com.example.mixologic.data.*
import com.example.mixologic.managers.AccountManager
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
        val profileIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_drink)
        view.setImageDrawable(profileIcon)
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
                view.text = cachedData.profileName
            } else {
                FirebaseManager.getUsersUserData(id)
                        .document("info")
                        .addSnapshotListener { value, error ->
                            if (value != null) {
                                val userData = value.toObject(UserData::class.java) ?: AccountManager.getDefaultData()
                                view.text = userData.name

                                GlobalScope.launch {
                                    val dataToCache = CachedData(id, userData.name, userData.profileImageURL)
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

                                GlobalScope.launch {
                                    val dataToCache = CachedData(id, userData.name, userData.profileImageURL)
                                    application.dataRepository.saveDataToCache(dataToCache)
                                }
                            }
                        }
            }
        }
    }
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