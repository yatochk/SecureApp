package com.yatochk.secure.app

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

open class MediaViewModel(
    private val imageSecureController: ImageSecureController,
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    protected lateinit var currentMedia: Image

    private val compositeDisposable = CompositeDisposable()

    private val mutableDelete = LiveEvent<Void>()
    val delete: LiveData<Void> = mutableDelete

    private val mutableToGallery = LiveEvent<Void>()
    val toGallery: LiveData<Void> = mutableToGallery

    private val mutableScanImage = LiveEvent<String>()
    val scanImage: LiveData<String> = mutableScanImage

    protected val mutableFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = mutableFinish

    private val mutableError = LiveEvent<ImageErrorType>()
    val imageError: LiveData<ImageErrorType> = mutableError

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
        mutableDelete.value = null
        compositeDisposable.add(
            Observable.just<Image>(currentMedia)
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
            Observable.just<Image>(currentMedia)
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