package com.yatochk.secure.app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import com.yatochk.secure.app.ui.main.MediaErrorType
import com.yatochk.secure.app.utils.DECRYPTED_POSTFIX
import com.yatochk.secure.app.utils.postEvent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

open class MediaViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesRepository: ImagesRepository,
    private val localizationManager: LocalizationManager
) : ViewModel() {

    private lateinit var currentMedia: Image

    private val compositeDisposable = CompositeDisposable()

    private val mutableDelete = LiveEvent<Void>()
    val delete: LiveData<Void> = mutableDelete

    private val mutableToGallery = LiveEvent<Void>()
    val toGallery: LiveData<Void> = mutableToGallery

    private val mutableScanImage = LiveEvent<String>()
    val scanImage: LiveData<String> = mutableScanImage

    private val eventFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = eventFinish

    private val mutableError = LiveEvent<String>()
    val mediaError: LiveData<String> = mutableError

    private val mutableOpenAlbumPicker = LiveEvent<Boolean>()
    val openAlbumPicker: LiveData<Boolean> = mutableOpenAlbumPicker

    private val eventNewAlbum = LiveEvent<Void>()
    val newAlbum: LiveData<Void> = eventNewAlbum

    lateinit var albums: LiveData<List<String>>
        private set

    open fun initMedia(image: Image) {
        currentMedia = image
        albums = imagesRepository.getAlbumsNames(currentMedia.album)
    }

    fun onPickAlbum(moveAlbum: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e(MediaViewModel::class.java.simpleName, throwable.localizedMessage, throwable)
            mutableError.value = localizationManager.getErrorString(MediaErrorType.TO_ALBUM)
        }) {
            val movedMedia = currentMedia.apply {
                album = moveAlbum
            }
            imagesRepository.updateImage(movedMedia)
            eventFinish.postEvent()
        }
    }

    fun animationEnd() {
        eventFinish.postEvent()
    }

    fun onclickNewAlbum() {
        eventNewAlbum.postEvent()
    }

    fun onCreateNewAlbum(newName: String) {
        onPickAlbum(newName)
    }

    fun onAlbumsPickCancel() {
        mutableOpenAlbumPicker.value = false
    }

    fun onDelete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e(MediaViewModel::class.java.simpleName, throwable.localizedMessage, throwable)
            mutableError.value = localizationManager.getErrorString(MediaErrorType.DELETE_IMAGE)
        }) {
            mutableDelete.value = null
            imagesRepository.deleteImage(currentMedia)
        }
    }

    fun clickRename() {
        mutableOpenAlbumPicker.value = true
    }

    protected open suspend fun mediaToGallery(media: Image) =
        coroutineScope {
            val regularImage = imageSecureController.decryptImageFromFile(media.securePath)
            val imageFile = File("${media.regularPath}$DECRYPTED_POSTFIX")
            val imageDirectory =
                File(media.regularPath.substring(0, media.regularPath.lastIndexOf("/")))
            imageDirectory.mkdirs()
            imageFile.writeBytes(regularImage)
            imageFile
        }

    fun onToGallery() {
        mutableToGallery.value = null
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e(MediaViewModel::class.java.simpleName, throwable.localizedMessage, throwable)
            mutableError.value = localizationManager.getErrorString(MediaErrorType.TO_GALLERY)
        }) {
            imagesRepository.deleteImage(currentMedia)
            mutableScanImage.value = mediaToGallery(currentMedia).path
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}