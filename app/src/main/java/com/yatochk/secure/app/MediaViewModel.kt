package com.yatochk.secure.app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import com.yatochk.secure.app.ui.main.MediaErrorType
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File

open class MediaViewModel(
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

    private val mutableFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = mutableFinish

    private val mutableError = LiveEvent<String>()
    val mediaError: LiveData<String> = mutableError

    private val mutableOpenRename = MutableLiveData<Boolean>()
    val openRename: LiveData<Boolean> = mutableOpenRename

    protected val mutableMedia = MutableLiveData<ByteArray>()

    fun initMedia(image: Image) {
        currentMedia = image
        mutableMedia.value = imageSecureController.decryptImageFromFile(image.securePath)
    }

    fun animationEnd() {
        mutableFinish.value = null
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

    }

    private suspend fun mediaToGallery(media: Image) =
        coroutineScope {
            val regularImage = imageSecureController.decryptImageFromFile(media.securePath)
            val imageFile = File(media.regularPath)
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
            val mediaFile = mediaToGallery(currentMedia)
            imagesRepository.deleteImage(currentMedia)
            mutableScanImage.value = mediaFile.path
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}