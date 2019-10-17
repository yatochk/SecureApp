package com.yatochk.secure.app.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.model.database.dao.ContactDao
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.utils.DEFAULT_IMAGE_ALBUM
import com.yatochk.secure.app.utils.DEFAULT_PHOTO_ALBUM
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesDao: ImagesDao,
    private val contactDao: ContactDao
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mutableShowError = LiveEvent<ImageErrorType>()
    val showImageError: LiveData<ImageErrorType> = mutableShowError

    private val mutableScanImage = LiveEvent<String>()
    val scanImage: LiveData<String> = mutableScanImage

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
                    mutableShowError.value = ImageErrorType.ADD_PHOTO
                }
            )
        )
    }

    fun receivedContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.addContact(contact)
        }
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
                    mutableShowError.value = ImageErrorType.ADD_IMAGE
                }
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}