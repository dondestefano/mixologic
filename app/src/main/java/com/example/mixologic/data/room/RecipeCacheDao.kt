package com.example.mixologic.data.room

import androidx.room.*
import com.example.mixologic.data.Recipe

@Dao
interface RecipeCacheDao {
    @Query("select * from recipe")
    fun getAll(): MutableList<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipe: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)

    @Query("DELETE FROM recipe")
    suspend fun deleteAll()
}