package com.yatochk.secure.app.ui.gallery

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.images.Album
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    imagesRepository: ImagesRepository,
    private val imageSecureController: ImageSecureController
) : ViewModel() {

    private val mutableOpenAlbum = LiveEvent<Pair<String, View>>()
    val openAlbum: LiveData<Pair<String, View>> = mutableOpenAlbum

    private val albumsExceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    private val albumsChanel = Channel<Album>()

    val albums: LiveData<List<Album>> = MediatorLiveData<List<Album>>().apply {
        addSource(imagesRepository.getImages()) { images ->
            value = emptyList()
            images.map { it.album }.toSet().forEach { name ->
                viewModelScope.launch(Dispatchers.IO + albumsExceptionHandler) {
                    val imageBytes = imageSecureController.decryptImageFromFile(
                        images.last { it.album == name }.securePath
                    )
                    albumsChanel.send(
                        Album(
                            name,
                            imageBytes
                        )
                    )
                }
            }
            viewModelScope.launch(albumsExceptionHandler) {
                for (album in albumsChanel) {
                    val newAlbums =
                        mutableListOf<Album>().apply {
                            value?.also { addAll(it) }
                            add(album)
                        }
                    value = newAlbums
                }
            }
        }
    }

    fun clickAlbum(album: String, view: View) {
        mutableOpenAlbum.value = Pair(album, view)
    }

}