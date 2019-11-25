package com.yatochk.secure.app.ui.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import com.yatochk.secure.app.ui.image.ImageViewModel
import com.yatochk.secure.app.utils.postEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    imageSecureController: ImageSecureController,
    imagesRepository: ImagesRepository
) : ImageViewModel(imageSecureController, imagesRepository) {

    private val mediatorVideo = MediatorLiveData<File>().apply {
        addSource(mutableMedia) {
            Single.just(it)
                .observeOn(Schedulers.io())
                .map {
                    File(currentMedia.securePath).also { file ->
                        file.mkdirs()
                        file.writeBytes(it)
                    }
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { videoFile -> value = videoFile },
                    { mutableFinish.postEvent() }
                )
        }
    }

    val video: LiveData<File> = mediatorVideo

}