package com.example.mixologic.features.search

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mixologic.data.Ingredient
import com.example.mixologic.data.Recipe
import com.example.mixologic.data.UserData
import com.example.mixologic.managers.AccountManager
import com.example.mixologic.managers.FirebaseManager
import com.squareup.picasso.Picasso

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Picasso
            .get()
            .load(imageUrl)
            .fit()
            .centerCrop()
            .into(view)
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
        if (id == AccountManager.getUser().uid) {
            view.text = AccountManager.getUsername()
        } else {
            FirebaseManager.getUsersUserData(id)
                    .document("info")
                    .addSnapshotListener { value, error ->
                        if (value != null) {
                            val userData = value.toObject(UserData::class.java)!!
                            view.text = userData.name
                        }
                    }
        }
    }
}

@BindingAdapter("getLikes")
fun getLikes(view: TextView, recipe: Recipe) {
    if (recipe.likes.isNullOrEmpty()) {
        view.text = "0"
    } else {
        view.text = recipe.likes!!.size.toString()
    }
}