package com.yatochk.secure.app.ui.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.yatochk.secure.app.MediaViewModel
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import com.yatochk.secure.app.utils.postEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    imageSecureController: ImageSecureController,
    imagesRepository: ImagesRepository
) : MediaViewModel(imageSecureController, imagesRepository) {

    private var videoFile: File? = null

    private val mediatorVideo = MediatorLiveData<File>().apply {
        addSource(mutableMedia) {
            Single.just(it)
                .subscribeOn(Schedulers.io())
                .map { bytes ->
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { videoFile -> value = videoFile },
                    { mutableFinish.postEvent() }
                )
        }
    }

    val video: LiveData<File> = mediatorVideo

    override fun onCleared() {
        super.onCleared()
        videoFile?.delete()
    }

}