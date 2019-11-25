package com.yatochk.secure.app.ui.image

import androidx.lifecycle.LiveData
import com.yatochk.secure.app.MediaViewModel
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.model.repository.ImagesRepository
import javax.inject.Inject


open class ImageViewModel @Inject constructor(
    imageSecureController: ImageSecureController,
    imagesRepository: ImagesRepository
) : MediaViewModel(imageSecureController, imagesRepository) {

    val image: LiveData<ByteArray> = mutableMedia

}