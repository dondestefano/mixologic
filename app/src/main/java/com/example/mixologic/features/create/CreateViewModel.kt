package com.example.mixologic.features.create

import androidx.lifecycle.ViewModel

class CreateViewModel(): ViewModel() {

    val liquor = listOf("Vodka", "Rum", "Gin", "Whiskey")
    val unit = listOf("ml", "cl", "dl", "st")

}