package com.yatochk.secure.app.ui.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.yatochk.secure.app.MediaViewModel
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import com.yatochk.secure.app.utils.postEvent
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    imageSecureController: ImageSecureController,
    imagesRepository: ImagesRepository,
    localizationManager: LocalizationManager
) : MediaViewModel(imageSecureController, imagesRepository, localizationManager) {

    private var videoFile: File? = null

    private suspend fun decryptVideo(bytes: ByteArray) = coroutineScope {
        withContext(Dispatchers.IO) {
            val videoDirectory =
                File(
                    currentMedia.regularPath.substring(
                        0,
                        currentMedia.regularPath.lastIndexOf("/")
                    )
                )
            videoDirectory.mkdirs()
            val videoFile = File(currentMedia.regularPath)
            videoFile.writeBytes(bytes)
            this@VideoViewModel.videoFile = videoFile
            this@VideoViewModel.videoFile
        }
    }

    private val mediatorVideo = MediatorLiveData<File>().apply {
        addSource(mutableMedia) {
            viewModelScope.launch(CoroutineExceptionHandler { _, _ ->
                mutableFinish.postEvent()
            }) {
                decryptVideo(it)?.also {
                    this@apply.value = videoFile
                }
            }
        }
    }

    val video: LiveData<File> = mediatorVideo

    override fun onCleared() {
        super.onCleared()
        videoFile?.delete()
    }

}