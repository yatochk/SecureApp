package com.yatochk.secure.app.ui.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yatochk.secure.app.MediaViewModel
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import javax.inject.Inject


open class ImageViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    imagesRepository: ImagesRepository,
    localizationManager: LocalizationManager
) : MediaViewModel(imageSecureController, imagesRepository, localizationManager) {

    private val mutableImage = MutableLiveData<ByteArray>()
    val image: LiveData<ByteArray> = mutableImage

    override fun initMedia(image: Image) {
        super.initMedia(image)
        mutableImage.value = imageSecureController.decryptImageFromFile(image.securePath)
    }

}