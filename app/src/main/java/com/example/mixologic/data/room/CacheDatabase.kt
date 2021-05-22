package com.example.mixologic.data.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mixologic.data.CachedData
import com.example.mixologic.data.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Recipe::class, CachedData::class], version = 5)
@TypeConverters(Converters::class)
abstract class CacheDatabase: RoomDatabase() {
    abstract val recipeCacheDao: RecipeCacheDao
    abstract val dataCacheDao: DataCacheDao

    companion object {
        @Volatile
        private var INSTANCE: CacheDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): CacheDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CacheDatabase::class.java,
                        "place_database"
                )
                        .fallbackToDestructiveMigration()
                        .addCallback(CacheDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        // Create the database if it doesn't exist.
        private class CacheDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
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