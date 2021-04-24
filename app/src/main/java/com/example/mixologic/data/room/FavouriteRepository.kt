package com.example.mixologic.data.room

import androidx.annotation.WorkerThread
import com.example.mixologic.data.Recipe

class FavouriteRepository(private val recipeCacheDao: RecipeCacheDao) {
    var favourites = listOf<Recipe>()

    fun getCachedFavourites() {
        favourites = recipeCacheDao.getAll()
    }

    @WorkerThread
    suspend fun saveToCache(recipe: Recipe) {
       recipeCacheDao.insert(recipe)
    }

    @WorkerThread
    fun deleteFromCache(recipe: Recipe) {
        recipeCacheDao.delete(recipe)
    }
}