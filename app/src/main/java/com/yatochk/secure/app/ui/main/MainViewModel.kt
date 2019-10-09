package com.yatochk.secure.app.ui.main

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.utils.REGULAR_FOLBER
import com.yatochk.secure.app.utils.SECURE_FOLBER
import com.yatochk.secure.app.utils.toTimeString
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesDao: ImagesDao
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val mutableOpenCamera = LiveEvent<Void>()

    private val mutableOpenGallery = LiveEvent<Void>()
    val openCamera: LiveData<Void> = mutableOpenCamera

    private val mutableShowError = LiveEvent<ErrorType>()
    val showError: LiveData<ErrorType> = mutableShowError

    private val mutableScanImage = LiveEvent<String>()
    val scanImage: LiveData<String> = mutableScanImage

    val openGallery: LiveData<Void> = mutableOpenGallery

    fun receivedPhoto(photo: Bitmap) {
        val securePath = Environment.getExternalStorageDirectory().absolutePath +
                SECURE_FOLBER
        val regularPath = Environment.getExternalStorageDirectory().absolutePath +
                REGULAR_FOLBER
        compositeDisposable.add(Observable.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                val photoName = "Photo_${Date().toTimeString()}.jpg"
                val buffer = ByteArrayOutputStream(photo.width * photo.height)
                photo.compress(CompressFormat.JPEG, 100, buffer)
                imageSecureController.encryptAndSaveImage(
                    buffer.toByteArray(),
                    securePath,
                    photoName
                )
                photoName
            }
            .map { name ->
                imagesDao.addImage(
                    Image(
                        securePath + name,
                        regularPath + name,
                        "main"
                    )
                )
                regularPath + name
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
                val securePath = Environment.getExternalStorageDirectory().absolutePath +
                        SECURE_FOLBER
                val imageFile = File(regularPath)
                require(imageFile.exists()) { "this file is not exist" }
                val imageBytes = imageFile.readBytes()
                imageFile.delete()
                imageSecureController.encryptAndSaveImage(
                    imageBytes,
                    securePath,
                    imageName
                )
            }
            .map {
                imagesDao.addImage(
                    Image(
                        it.path,
                        regularPath,
                        "main"
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