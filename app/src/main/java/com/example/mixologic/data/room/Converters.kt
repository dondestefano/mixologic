package com.example.mixologic.data.room

import androidx.room.TypeConverter
import com.example.mixologic.data.Ingredient
import com.example.mixologic.data.Like
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    var gson = Gson()

    // Handle Ingredient
    @TypeConverter
    fun toIngredient(json: String): List<Ingredient> {
        val type = object : TypeToken<List<Ingredient>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun ingredientToJson(torrent: List<Ingredient>): String {
        val type = object: TypeToken<List<Ingredient>>() {}.type
        return gson.toJson(torrent, type)
    }

    // Handle Like
    @TypeConverter
    fun toLike(json: String): List<Like> {
        val type = object : TypeToken<List<Like>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun likeToJson(torrent: List<Like>): String {
        val type = object: TypeToken<List<Like>>() {}.type
        return gson.toJson(torrent, type)
    }
}