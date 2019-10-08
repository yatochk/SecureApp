package com.yatochk.secure.app.ui.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import com.yatochk.secure.app.ui.main.ErrorType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImageVIewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var currentImage: Image
    private val mediatorImage = MutableLiveData<Bitmap>()

    val image: LiveData<Bitmap> = mediatorImage
    private val mutableDelete = LiveEvent<Void>()

    val delete: LiveData<Void> = mutableDelete
    private val mutableFinish = LiveEvent<Void>()

    val finish: LiveData<Void> = mutableFinish
    private val mutableError = LiveEvent<ErrorType>()

    val error: LiveData<ErrorType> = mutableError

    fun animationEnd() {
        mutableFinish.value = null
    }

    fun initImage(image: Image) {
        currentImage = image
        val decryptedBytes = imageSecureController.decryptImage(image.path)
        mediatorImage.value = BitmapFactory.decodeByteArray(
            decryptedBytes,
            0,
            decryptedBytes.size
        )
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
                        mutableError.value = ErrorType.DELETE_IMAGE
                    }
                )
        )
    }

    fun onRename() {

    }

    fun onToGallery() {
        mutableDelete.value = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}