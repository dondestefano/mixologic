package com.example.mixologic.data.room

import com.example.mixologic.data.CachedImage

class ImageRepository(private val imageCacheDao: ImageCacheDao) {
    var cachedImages = listOf<CachedImage>()

    fun getCachedImages() {
        cachedImages = imageCacheDao.getAll()
    }

    suspend fun saveImageToCache(cachedImage: CachedImage) {
        imageCacheDao.insert(cachedImage)
        getCachedImages()
    }

    suspend fun deleteImagesFromCache(){
        imageCacheDao.deleteAll()
    }

    fun getImage(userId: String): CachedImage? {
        cachedImages.forEachIndexed { _, image ->
            if (image.id == userId) {
                return image
            }
        }
        return null
    }
}