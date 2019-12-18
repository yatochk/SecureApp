package com.yatochk.secure.app.model.repository

import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import java.io.File
import javax.inject.Inject

class ImagesRepository @Inject constructor(
    private val imagesDao: ImagesDao
) {

    suspend fun deleteImage(image: Image) {
        File(image.securePath).also {
            if (it.exists())
                it.delete()
        }
        imagesDao.deleteImage(image)
    }

    suspend fun updateImage(image: Image) = imagesDao.updateImage(image)

    fun getImages() = imagesDao.getImages()

    fun getAnotherAlbums(exclude: String) = imagesDao.getAlbumsExcept(exclude)

}