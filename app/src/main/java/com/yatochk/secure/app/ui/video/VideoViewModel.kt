package com.yatochk.secure.app.ui.video

import com.yatochk.secure.app.MediaViewModel
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import kotlinx.coroutines.coroutineScope
import java.io.File
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    imageSecureController: ImageSecureController,
    imagesRepository: ImagesRepository,
    localizationManager: LocalizationManager
) : MediaViewModel(imageSecureController, imagesRepository, localizationManager) {

    override suspend fun mediaToGallery(media: Image): File =
        coroutineScope {
            val secureVideo = File(media.securePath)
            require(secureVideo.exists()) { "video is not exist" }
            val regularFile = File(media.regularPath)
            regularFile.mkdirs()
            secureVideo.copyTo(regularFile, overwrite = true)
            secureVideo.delete()
            regularFile
        }

}