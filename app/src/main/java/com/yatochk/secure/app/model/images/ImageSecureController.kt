package com.yatochk.secure.app.model.images

import com.yatochk.secure.app.model.Cypher
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageSecureController @Inject constructor(
    private val cypher: Cypher
) {

    fun encryptImage(bytes: ByteArray, path: String, name: String): File {
        val directory = File(path)
        directory.mkdirs()
        return File(path + name).apply {
            writeBytes(cypher.encrypt(bytes))
        }
    }

    fun encryptImage(path: String): File {
        val imageFile = File(path)
        require(imageFile.exists()) { "this file is not exist" }
        val imageBytes = imageFile.readBytes()
        imageFile.delete()
        return File(path).apply {
            writeBytes(cypher.encrypt(imageBytes))
        }
    }

    fun decryptImage(path: String): ByteArray {
        val imageFile = File(path)
        require(imageFile.exists()) { "this file is not exist" }
        val imageBytes = imageFile.readBytes()
        return cypher.decrypt(imageBytes)
    }

}