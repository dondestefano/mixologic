package com.example.mixologic.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient (
        val name: String? = null,
        val amount: Int? = null,
        val unit: String? = null,
): Parcelable