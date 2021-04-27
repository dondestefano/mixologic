package com.example.mixologic.managers

import com.example.mixologic.application.MixologicApplication
import com.example.mixologic.data.Like
import com.example.mixologic.data.Recipe
import com.google.firebase.firestore.FieldValue

object LikeManager {
    lateinit var application: MixologicApplication

    suspend fun handleOnLiked(recipe: Recipe, userId: String) {
        if(application.checkNetwork()) {
            if (recipe.likes == null) {
                initialLike(recipe, userId)
            } else {
                recipe.likes?.let { likes ->
                    if (!hasUserLiked(likes, userId)) {
                        likeRecipe(recipe, userId)
                        application.favouriteRepository.saveToCache(recipe)
                    } else {
                        unlikeRecipe(recipe, userId)
                        application.favouriteRepository.deleteFromCache(recipe)
                    }
                }
            }
        }
    }

    private fun likeRecipe(recipe: Recipe, userId: String) {
        recipe.id?.let {
            FirebaseManager.getRecipe(it)
                    .update("likes", FieldValue.arrayUnion(Like(userId)))
        }
    }

    private fun unlikeRecipe(recipe: Recipe, userId: String) {
        recipe.id?.let {
                FirebaseManager.getRecipe(it)
                    .update("likes", FieldValue.arrayRemove(Like(userId)))
        }
    }

    private fun initialLike(recipe: Recipe, userId: String) {
        val likes = listOf(Like(userId))
        recipe.id?.let {
            FirebaseManager.getRecipe(it)
                    .update("likes", likes)
        }
    }

    fun hasUserLiked(likes: List<Like>, userId: String): Boolean {
        val like = Like(userId)
        return (likes.contains(like))
    }
}