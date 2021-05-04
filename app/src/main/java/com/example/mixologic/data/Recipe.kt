package com.example.mixologic.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "recipe")
@Parcelize
data class Recipe (
        @PrimaryKey val id: String = "",
        val name: String? = null,
        val preparation: String? = null,
        val liquors: List<Ingredient>? = null,
        val ingredients: List<Ingredient>? = null,
        val creatorId: String? = null,
        val imageURL: String? = null,
        var likes: MutableList<Like>? = null ): Parcelable