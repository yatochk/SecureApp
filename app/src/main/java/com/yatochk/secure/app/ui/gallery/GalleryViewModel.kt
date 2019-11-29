package com.yatochk.secure.app.ui.gallery

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
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

    val albums: LiveData<List<Album>> = MediatorLiveData<List<Album>>().apply {
        addSource(imagesRepository.getImages()) { images ->
            val displayImages = arrayListOf<Album>()
            images.map { it.album }.toSet().forEach { name ->
                try {
                    val imageBytes = imageSecureController.decryptImageFromFile(
                        images.last { it.album == name }.securePath
                    )
                    displayImages.add(
                        Album(
                            name,
                            imageBytes
                        )
                    )
                } catch (e: Throwable) {

                }
            }
            value = displayImages
        }
    }

    fun clickAlbum(album: String, view: View) {
        mutableOpenAlbum.value = Pair(album, view)
    }

}