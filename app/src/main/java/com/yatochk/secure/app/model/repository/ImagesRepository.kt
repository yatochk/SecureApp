package com.yatochk.secure.app.model.repository

import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import java.io.File
import javax.inject.Inject

class ImagesRepository @Inject constructor(
    private val imagesDao: ImagesDao
) {

    fun deleteImage(image: Image) {
        File(image.securePath).delete()
        imagesDao.deleteImage(image)
    }

    fun getImages() = imagesDao.getImages()

}