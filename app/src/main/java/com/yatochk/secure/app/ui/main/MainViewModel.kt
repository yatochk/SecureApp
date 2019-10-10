package com.yatochk.secure.app.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.utils.DEFAULT_IMAGE_ALBUM
import com.yatochk.secure.app.utils.DEFAULT_PHOTO_ALBUM
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesDao: ImagesDao
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mutableOpenCamera = LiveEvent<Void>()
    val openCamera: LiveData<Void> = mutableOpenCamera

    private val mutableShowError = LiveEvent<ErrorType>()
    val showError: LiveData<ErrorType> = mutableShowError

    private val mutableScanImage = LiveEvent<String>()
    val scanImage: LiveData<String> = mutableScanImage

    private val mutableOpenGallery = LiveEvent<Void>()
    val openGallery: LiveData<Void> = mutableOpenGallery

    fun receivedPhoto(receivedName: String) {
        compositeDisposable.add(Observable.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                val imageFile = File(ImageSecureController.regularPath + receivedName)
                val imageBytes = imageFile.readBytes()
                val photoName = imageFile.name
                imageSecureController.encryptAndSaveImage(
                    imageBytes,
                    photoName
                )
                imageFile.delete()
                photoName
            }
            .map { name ->
                imagesDao.addImage(
                    Image(
                        ImageSecureController.securePath + name,
                        ImageSecureController.regularPath + name,
                        DEFAULT_PHOTO_ALBUM
                    )
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.i(MainViewModel::class.java.name, "Photo encrypt and saved")
                },
                {
                    Log.e("Error on securing", it.localizedMessage, it)
                    mutableShowError.value = ErrorType.ADD_PHOTO
                }
            )
        )
    }

    fun receivedGalleryImage(regularPath: String) {
        compositeDisposable.add(Observable.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                val imageName = regularPath.substring(regularPath.lastIndexOf("/") + 1)
                val imageFile = File(regularPath)
                require(imageFile.exists()) { "this file is not exist" }
                val imageBytes = imageFile.readBytes()
                imageFile.delete()
                imageSecureController.encryptAndSaveImage(
                    imageBytes,
                    imageName
                )
            }
            .map {
                imagesDao.addImage(
                    Image(
                        it.path,
                        regularPath,
                        DEFAULT_IMAGE_ALBUM
                    )
                )
                regularPath
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mutableScanImage.value = it },
                {
                    Log.e("Error on securing", it.localizedMessage, it)
                    mutableShowError.value = ErrorType.ADD_IMAGE
                }
            )
        )
    }

    fun clickPhoto() {
        mutableOpenCamera.value = null
    }

    fun clickGallery() {
        mutableOpenGallery.value = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}