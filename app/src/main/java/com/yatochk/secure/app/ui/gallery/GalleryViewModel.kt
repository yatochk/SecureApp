package com.yatochk.secure.app.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.snakydesign.livedataextensions.map
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.ImageSecureController
import java.io.File
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val imagesDao: ImagesDao,
    private val imageSecureController: ImageSecureController
) : ViewModel() {

    val images: LiveData<List<File>> = imagesDao.getImages()
        .map { list ->
            list.map {
                imageSecureController.decodeImage(it)
            }
        }

}