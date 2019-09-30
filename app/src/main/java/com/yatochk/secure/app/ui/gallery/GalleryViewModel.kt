package com.yatochk.secure.app.ui.gallery

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.snakydesign.livedataextensions.map
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Album
import com.yatochk.secure.app.model.images.ImageSecureController
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    imagesDao: ImagesDao,
    private val imageSecureController: ImageSecureController
) : ViewModel() {

    val albums: LiveData<List<Album>> = imagesDao.getImages().map { images ->
        images.map { it.album }.toSet().map { name ->
            val imageBytes = imageSecureController.decodeImage(
                images.last { it.album == name }
            )
            Album(
                name,
                BitmapFactory.decodeByteArray(
                    imageBytes,
                    0,
                    imageBytes.size
                )
            )
        }
    }
}