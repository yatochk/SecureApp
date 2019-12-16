package com.yatochk.secure.app.ui.video

import com.yatochk.secure.app.MediaViewModel
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import com.yatochk.secure.app.utils.DECRYPTED_POSTFIX
import com.yatochk.secure.app.utils.REGULAR_VIDEO_FORMAT
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
            File(media.regularPath).also {
                if (it.exists()) {
                    val regularPath = it.path
                    val newName =
                        regularPath.substring(0, regularPath.indexOf(".$REGULAR_VIDEO_FORMAT"))
                    val renamedFile = File("${newName}$DECRYPTED_POSTFIX.$REGULAR_VIDEO_FORMAT")
                    if (!renamedFile.exists()) {
                        it.renameTo(renamedFile)
                    }
                }
            }
        }

}