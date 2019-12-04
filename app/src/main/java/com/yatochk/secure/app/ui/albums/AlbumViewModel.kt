package com.yatochk.secure.app.ui.albums

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.main.ImageErrorType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    private val compositeDisposable = CompositeDisposable()

    fun screenOpened() {
        mutableStartObserving.value = null
    }

    private fun decryptImages(images: List<Image>) {
        mediatorImages.value = emptyList()
        compositeDisposable.add(
            Observable.fromIterable(images)
                .subscribeOn(Schedulers.io())
                .map {
                    val decryptedBytes = imageSecureController.decryptImageFromFile(it.securePath)
                    Pair(it, decryptedBytes)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { newImage ->
                        val newImages = mutableListOf<Pair<Image, ByteArray>>().apply {
                            mediatorImages.value?.also { addAll(it) }
                            add(newImage)
                        }
                        mediatorImages.value = newImages
                    },
                    {
                        mutableShowError.value =
                            localizationManager.getErrorString(ImageErrorType.ENCRYPT_IMAGE)
                    }
                )
        )
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
