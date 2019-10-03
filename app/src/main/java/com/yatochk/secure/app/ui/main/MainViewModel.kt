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
import com.yatochk.secure.app.utils.SECURE_FOLBER
import com.yatochk.secure.app.utils.toTimeString
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
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

    private val mutableShowSuccess = LiveEvent<SuccessType>()
    val showSuccess: LiveData<SuccessType> = mutableShowSuccess

    val openGallery: LiveData<Void> = mutableOpenGallery

    fun receivedPhoto(photo: Bitmap) {
        compositeDisposable.add(Observable.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                val photoName = "Photo_${Date().toTimeString()}"
                val buffer = ByteArrayOutputStream(photo.width * photo.height)
                val path = Environment.getExternalStorageDirectory().absolutePath +
                        SECURE_FOLBER
                photo.compress(CompressFormat.PNG, 100, buffer)
                imageSecureController.encryptImage(
                    buffer.toByteArray(),
                    path,
                    photoName
                )
                path + photoName
            }
            .map {
                imagesDao.addImage(
                    Image(
                        it,
                        "main"
                    )
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mutableShowSuccess.value = SuccessType.ADD_PHOTO },
                {
                    Log.e("Error on securing", it.localizedMessage, it)
                    mutableShowError.value = ErrorType.ADD_PHOTO
                }
            )
        )
    }

    fun receivedGalleryImage(path: String) {
        compositeDisposable.add(Observable.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                imageSecureController.encryptImage(path)
            }
            .map {
                imagesDao.addImage(
                    Image(
                        path,
                        "main"
                    )
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mutableShowSuccess.value = SuccessType.ADD_IMAGE },
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