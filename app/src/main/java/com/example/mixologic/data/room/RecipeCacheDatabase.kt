package com.example.mixologic.data.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mixologic.data.CachedData
import com.example.mixologic.data.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Recipe::class, CachedData::class], version = 4)
@TypeConverters(Converters::class)
abstract class RecipeCacheDatabase: RoomDatabase() {
    abstract val recipeCacheDao: RecipeCacheDao
    abstract val dataCacheDao: DataCacheDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeCacheDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): RecipeCacheDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecipeCacheDatabase::class.java,
                        "place_database"
                )
                        .fallbackToDestructiveMigration()
                        .addCallback(RecipeCacheDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        // Create the database if it doesn't exist.
        private class RecipeCacheDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        clearDatabase(database.recipeCacheDao)
                    }
                }
            }
        }

        suspend fun clearDatabase(recipeCacheDao: RecipeCacheDao) {
            recipeCacheDao.deleteAll()
        }
    }
}