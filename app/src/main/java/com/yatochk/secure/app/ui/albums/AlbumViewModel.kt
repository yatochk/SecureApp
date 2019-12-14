package com.yatochk.secure.app.ui.albums

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.widget.ImageView
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.main.MediaErrorType
import com.yatochk.secure.app.utils.isVideoPath
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesDao: ImagesDao,
    private val localizationManager: LocalizationManager
) : ViewModel() {

    private val mediatorImages = MediatorLiveData<List<Pair<Image, Bitmap>>>()
    val images: LiveData<List<Pair<Image, Bitmap>>> = mediatorImages

    private val mutableShowError = LiveEvent<String>()
    val showImageError: LiveData<String> = mutableShowError

    private val mutableFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = mutableFinish

    private val mutableOpenImage = LiveEvent<Pair<Image, ImageView>>()
    val openImage: LiveData<Pair<Image, ImageView>> = mutableOpenImage

    private val mutableOpenVideo = LiveEvent<String>()
    val openVideo: LiveData<String> = mutableOpenVideo

    private val mutableStartObserving = MutableLiveData<Void>()
    val startObserving: LiveData<Void> = mutableStartObserving

    private val mutableDecryptionSeek = MutableLiveData<DecryptionSeek>()
    val decryptionSeek: LiveData<DecryptionSeek> = mutableDecryptionSeek

    fun screenOpened() {
        mutableStartObserving.value = null
    }

    private val chanel = Channel<Pair<Image, Bitmap>>()

    override fun onCleared() {
        super.onCleared()
        chanel.close()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        mutableShowError.postValue(localizationManager.getErrorString(MediaErrorType.ENCRYPT_MEDIA))
    }

    private fun decryptImages(images: List<Image>) {
        mutableDecryptionSeek.value = DecryptionSeek(images.size, 0, true)
        mediatorImages.value = emptyList()
        images.forEach {
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                val bitmap = if (it.regularPath.isVideoPath()) {
                    ThumbnailUtils.createVideoThumbnail(
                        imageSecureController.decryptVideo(it).absolutePath,
                        MediaStore.Images.Thumbnails.MINI_KIND
                    )
                } else {
                    val decryptedBytes =
                        imageSecureController.decryptImageFromFile(it.securePath)
                    BitmapFactory.decodeByteArray(
                        decryptedBytes,
                        0,
                        decryptedBytes.size
                    )
                }
                val newImage = Pair(it, bitmap)
                chanel.send(newImage)
            }
        }
        viewModelScope.launch(exceptionHandler) {
            for (image in chanel) {
                val newImages =
                    mutableListOf<Pair<Image, Bitmap>>().apply {
                        mediatorImages.value?.also { addAll(it) }
                        add(image)
                    }
                mediatorImages.value = newImages
                val decryptedCount = newImages.size
                mutableDecryptionSeek.value =
                    DecryptionSeek(
                        images.size,
                        decryptedCount,
                        decryptedCount != images.size
                    )
            }
        }
    }

    fun initAlbum(albumName: String) {
        mediatorImages.addSource(imagesDao.getImages(albumName)) { images ->
            if (images.isNullOrEmpty()) {
                mutableFinish.value = null
            } else {
                decryptImages(images)
            }
        }
    }

    fun clickImage(image: Image, imageView: ImageView) {
        if (image.regularPath.isVideoPath()) {
            mutableOpenVideo.value = image.regularPath
        } else {
            mutableOpenImage.value = Pair(image, imageView)
        }
    }

}
