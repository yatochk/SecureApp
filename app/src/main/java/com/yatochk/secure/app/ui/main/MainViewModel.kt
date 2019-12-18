package com.yatochk.secure.app.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.utils.DEFAULT_CAMERA_ALBUM
import com.yatochk.secure.app.utils.DEFAULT_GALLERY_ALBUM
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesDao: ImagesDao,
    private val localizationManager: LocalizationManager
) : ViewModel() {

    private val mutableShowError = LiveEvent<String>()
    val showImageError: LiveData<String> = mutableShowError

    private val mutableScanImage = LiveEvent<String>()
    val scanImage: LiveData<String> = mutableScanImage

    fun receivedPhoto(receivedName: String) {
        receivedMedia(receivedName, receivedName)
    }

    fun receivedVideo(receivedName: String) {
        val nameForSave = "${receivedName.substring(0, receivedName.lastIndexOf("."))}.mp4"
        receivedMedia(
            receivedName,
            nameForSave
        )
    }

    private fun receivedMedia(receivedName: String, saveName: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e("Error on securing", throwable.localizedMessage, throwable)
            mutableShowError.value = localizationManager.getErrorString(MediaErrorType.ADD_PHOTO)
        }) {
            val name = encryptMedia(receivedName)
            imagesDao.addImage(
                Image(
                    ImageSecureController.securePath + name,
                    ImageSecureController.regularPath + saveName,
                    DEFAULT_CAMERA_ALBUM
                )
            )
            Log.i(MainViewModel::class.java.name, "Photo encrypt and saved")
        }
    }

    private suspend fun encryptMedia(name: String): String =
        coroutineScope {
            val imageFile = File(ImageSecureController.regularPath + name)
            val imageBytes = imageFile.readBytes()
            val photoName = imageFile.name
            imageSecureController.encryptAndSaveImage(
                imageBytes,
                photoName
            )
            imageFile.delete()
            photoName
        }

    private suspend fun encryptGalleryMedia(regularPath: String): File =
        coroutineScope {
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

    fun receivedGalleryImage(regularPath: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e("Error on securing", throwable.localizedMessage, throwable)
            mutableShowError.value = localizationManager.getErrorString(MediaErrorType.ADD_IMAGE)
        }) {
            val file = encryptGalleryMedia(regularPath)
            imagesDao.addImage(
                Image(
                    file.path,
                    regularPath,
                    DEFAULT_GALLERY_ALBUM
                )
            )
            mutableScanImage.value = regularPath
        }
    }

    fun receivedGalleryVideo(regularPath: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e("Error on securing", throwable.localizedMessage, throwable)
            mutableShowError.value = localizationManager.getErrorString(MediaErrorType.ADD_IMAGE)
        }) {
            val file = encryptGalleryMedia(regularPath)
            imagesDao.addImage(
                Image(
                    file.path,
                    regularPath,
                    DEFAULT_GALLERY_ALBUM
                )
            )
            mutableScanImage.value = regularPath
        }
    }

}