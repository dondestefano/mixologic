package com.example.mixologic.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mixologic.data.CachedData

@Dao
interface DataCacheDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: CachedData)

    @Query("select * from data")
    fun getAll(): MutableList<CachedData>

    @Query("SELECT * from data WHERE id = :id")
    fun get(id: String): CachedData?

    @Query("DELETE FROM data")
    suspend fun deleteAll()
}