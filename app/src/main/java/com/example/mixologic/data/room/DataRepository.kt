package com.example.mixologic.data.room

import com.example.mixologic.data.CachedData

class DataRepository(private val dataCacheDao: DataCacheDao) {
    var cachedData = listOf<CachedData>()

    fun getCachedData() {
        cachedData = dataCacheDao.getAll()
    }

    suspend fun saveDataToCache(cachedData: CachedData) {
        dataCacheDao.insert(cachedData)
        getCachedData()
    }

    suspend fun deleteDataFromCache(){
        dataCacheDao.deleteAll()
    }

    fun getData(userId: String): CachedData? {
        cachedData.forEachIndexed { _, data ->
            if (data.id == userId) {
                return data
            }
        }
        return null
    }
}