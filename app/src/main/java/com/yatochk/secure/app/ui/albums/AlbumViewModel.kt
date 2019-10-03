package com.yatochk.secure.app.ui.albums

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.ImageSecureController
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesDao: ImagesDao
) : ViewModel() {

    private val mediatorImages = MediatorLiveData<List<Bitmap>>()
    val images: LiveData<List<Bitmap>> = mediatorImages

    fun setAlbum(albumName: String) {
        mediatorImages
            .addSource(imagesDao.getImages(albumName)) { images ->
                mediatorImages.value = images.map {
                    val decryptedBytes = imageSecureController.decryptImage(it)
                    BitmapFactory.decodeByteArray(
                        decryptedBytes,
                        0,
                        decryptedBytes.size
                    )
                }
            }
    }

}
