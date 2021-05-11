package com.example.mixologic.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "data")
@Parcelize
data class CachedData (
        @PrimaryKey val id : String = "",
        val profileName: String? = null,
        val profileImageURL: String? = null
): Parcelable