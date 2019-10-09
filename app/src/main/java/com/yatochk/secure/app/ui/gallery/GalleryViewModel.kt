package com.yatochk.secure.app.ui.gallery

import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.snakydesign.livedataextensions.map
import com.yatochk.secure.app.model.images.Album
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    imagesRepository: ImagesRepository,
    private val imageSecureController: ImageSecureController
) : ViewModel() {

    private val mutableOpenAlbum = LiveEvent<Pair<String, View>>()
    val openAlbum: LiveData<Pair<String, View>> = mutableOpenAlbum

    val albums: LiveData<List<Album>> = imagesRepository.getImages().map { images ->
        images.map { it.album }.toSet().map { name ->
            val imageBytes = imageSecureController.decryptImageFromFile(
                images.last { it.album == name }.securePath
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

    fun clickAlbum(album: String, view: View) {
        mutableOpenAlbum.value = Pair(album, view)
    }

}