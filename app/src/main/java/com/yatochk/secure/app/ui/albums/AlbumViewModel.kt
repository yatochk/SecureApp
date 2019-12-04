package com.yatochk.secure.app.ui.albums

import android.widget.ImageView
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.main.MediaErrorType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesDao: ImagesDao,
    private val localizationManager: LocalizationManager
) : ViewModel() {

    private val mediatorImages = MediatorLiveData<List<Pair<Image, ByteArray>>>()
    val images: LiveData<List<Pair<Image, ByteArray>>> = mediatorImages

    private val mutableShowError = LiveEvent<String>()
    val showImageError: LiveData<String> = mutableShowError

    private val mutableFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = mutableFinish

    private val mutableOpenImage = LiveEvent<Pair<Image, ImageView>>()
    val openImage: LiveData<Pair<Image, ImageView>> = mutableOpenImage

    private val mutableOpenVideo = LiveEvent<Image>()
    val openVideo: LiveData<Image> = mutableOpenVideo

    private val mutableStartObserving = MutableLiveData<Void>()
    val startObserving: LiveData<Void> = mutableStartObserving

    fun screenOpened() {
        mutableStartObserving.value = null
    }

    private fun decryptImages(images: List<Image>) {
        mediatorImages.value = emptyList()
        viewModelScope.launch(CoroutineExceptionHandler { _, _ ->
            mutableShowError.value =
                localizationManager.getErrorString(MediaErrorType.ENCRYPT_MEDIA)
        }) {
            images.forEach {
                launch(Dispatchers.IO) {
                    val decryptedBytes = imageSecureController.decryptImageFromFile(it.securePath)
                    val newImage = Pair(it, decryptedBytes)
                    val newImages = mutableListOf<Pair<Image, ByteArray>>().apply {
                        mediatorImages.value?.also { addAll(it) }
                        add(newImage)
                    }
                    mediatorImages.postValue(newImages)
                }
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
        if (image.regularPath.contains("MPEG_4")) {
            mutableOpenVideo.value = image
        } else {
            mutableOpenImage.value = Pair(image, imageView)
        }
    }

}
