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
import com.yatochk.secure.app.utils.changeFormat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Error on securing", throwable.localizedMessage, throwable)
        mutableShowError.postValue(localizationManager.getErrorString(MediaErrorType.ADD_IMAGE))
    }

    fun receivedPhoto(receivedName: String) {
        receivedMedia(receivedName, receivedName)
    }

    fun receivedVideo(receivedName: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val nameForSave = "${receivedName.substring(0, receivedName.lastIndexOf("."))}.mp4"
            val imageFile = File(ImageSecureController.regularPath + receivedName)
            val secureFile = File(ImageSecureController.securePath + imageFile.name + ".txt")
            val directory = File(ImageSecureController.securePath)
            directory.mkdirs()

            imageFile.copyTo(secureFile, true)
            imageFile.delete()

            imagesDao.addImage(
                Image(
                    ImageSecureController.securePath + secureFile.name,
                    ImageSecureController.regularPath + nameForSave,
                    DEFAULT_CAMERA_ALBUM
                )
            )
        }
    }

    private fun receivedMedia(receivedName: String, saveName: String) {
        viewModelScope.launch(exceptionHandler) {
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
        withContext(Dispatchers.IO) {
            val imageFile = File(ImageSecureController.regularPath + name)
            val secureFile = File(ImageSecureController.securePath + imageFile.name)
            val secureImage = imageSecureController.encryptAndSaveFile(imageFile, secureFile)
            imageFile.delete()
            secureImage.name
        }

    private suspend fun encryptGalleryMedia(regularPath: String): File =
        withContext(Dispatchers.IO) {
            val imageName = regularPath.substring(regularPath.lastIndexOf("/") + 1)
            val imageFile = File(regularPath)
            require(imageFile.exists()) { "this file is not exist" }

            val secureFile = File(ImageSecureController.securePath + imageName)
            val encryptedFile = imageSecureController.encryptAndSaveFile(imageFile, secureFile)

            imageFile.delete()
            encryptedFile
        }

    fun receivedGalleryImage(regularPath: String) {
        viewModelScope.launch(exceptionHandler) {
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
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val imageFile = File(regularPath)
            val secureName = imageFile.name.changeFormat(ImageSecureController.FAKE_FORMAT)
            val secureFile =
                File(ImageSecureController.securePath + secureName)
            val directory = File(ImageSecureController.securePath)
            directory.mkdirs()

            imageFile.copyTo(secureFile, true)
            imageFile.delete()
            imagesDao.addImage(
                Image(
                    secureFile.path,
                    regularPath,
                    DEFAULT_GALLERY_ALBUM
                )
            )
            mutableScanImage.postValue(regularPath)
        }
    }

}