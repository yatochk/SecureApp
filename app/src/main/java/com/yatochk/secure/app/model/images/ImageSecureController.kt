package com.yatochk.secure.app.model.images

import java.io.File
import javax.inject.Inject

class ImageSecureController @Inject constructor() {

    fun encodeImage(image: Image): File {
        //TODO
        return File(image.path)
    }

    fun decodeImage(image: Image): File {
        //TODO
        return File(image.path)
    }

}