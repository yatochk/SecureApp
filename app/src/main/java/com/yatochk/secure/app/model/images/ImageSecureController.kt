package com.yatochk.secure.app.model.images

import com.yatochk.secure.app.model.Cypher
import java.io.File
import javax.inject.Inject

class ImageSecureController @Inject constructor(
    private val cypher: Cypher
) {

    fun encryptImage(image: Image): File {
        val imageFile = File(image.path)
        require(imageFile.exists()) { "this file is not exist" }
        val imageBytes = imageFile.readBytes()
        imageFile.delete()
        return File(image.path).apply {
            writeBytes(cypher.encrypt(imageBytes))
        }
    }

    fun decodeImage(image: Image): ByteArray {
        val imageFile = File(image.path)
        require(imageFile.exists()) { "this file is not exist" }
        val imageBytes = imageFile.readBytes()
        return cypher.decrypt(imageBytes)
    }

}