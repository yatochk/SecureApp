package com.yatochk.secure.app.ui.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import com.yatochk.secure.app.ui.main.ImageErrorType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject


class ImageVIewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var currentImage: Image

    private val mediatorImage = MutableLiveData<ByteArray>()
    val image: LiveData<ByteArray> = mediatorImage

    private val mutableDelete = LiveEvent<Void>()
    val delete: LiveData<Void> = mutableDelete

    private val mutableToGallery = LiveEvent<Void>()
    val toGallery: LiveData<Void> = mutableToGallery

    private val mutableScanImage = LiveEvent<String>()
    val scanImage: LiveData<String> = mutableScanImage

    private val mutableFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = mutableFinish

    private val mutableError = LiveEvent<ImageErrorType>()
    val imageError: LiveData<ImageErrorType> = mutableError

    private val mutableOpenRename = MutableLiveData<Boolean>()
    val openRename: LiveData<Boolean> = mutableOpenRename

    fun animationEnd() {
        mutableFinish.value = null
    }

    fun initImage(image: Image) {
        currentImage = image
        mediatorImage.value = imageSecureController.decryptImageFromFile(image.securePath)
    }

    fun onDelete() {
        mutableDelete.value = null
        compositeDisposable.add(
            Observable.just<Image>(currentImage)
                .subscribeOn(Schedulers.io())
                .map {
                    imagesRepository.deleteImage(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {

                    },
                    {
                        mutableError.value = ImageErrorType.DELETE_IMAGE
                    }
                )
        )
    }

    fun clickRename() {

    }

    fun onToGallery() {
        mutableToGallery.value = null
        compositeDisposable.add(
            Observable.just<Image>(currentImage)
                .subscribeOn(Schedulers.io())
                .map {
                    val regularImage = imageSecureController.decryptImageFromFile(it.securePath)
                    val imageFile = File(it.regularPath)
                    val imageDirectory =
                        File(it.regularPath.substring(0, it.regularPath.lastIndexOf("/")))
                    imageDirectory.mkdirs()
                    imageFile.writeBytes(regularImage)
                    imagesRepository.deleteImage(it)
                    imageFile
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        mutableScanImage.value = it.path
                    },
                    {
                        mutableError.value = ImageErrorType.TO_GALLERY
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}