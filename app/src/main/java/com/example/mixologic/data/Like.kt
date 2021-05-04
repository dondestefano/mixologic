package com.example.mixologic.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Like (
        val userId: String? = null,
): Parcelable