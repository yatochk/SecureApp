package com.yatochk.secure.app.model.images

import android.os.Environment
import com.yatochk.secure.app.model.Cypher
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageSecureController @Inject constructor(
    private val cypher: Cypher
) {

    companion object {
        private const val SECURE_FOLDER = "/calculator_need_container/"
        private const val REGULAR_FOLDER = "/photo/"

        val regularPath = Environment.getExternalStorageDirectory().absolutePath +
                REGULAR_FOLDER

        val securePath = Environment.getExternalStorageDirectory().absolutePath +
                SECURE_FOLDER

    }

    fun encryptAndSaveImage(bytes: ByteArray, name: String): File {
        val directory = File(securePath)
        directory.mkdirs()
        return File(securePath + name).apply {
            writeBytes(cypher.encrypt(bytes))
        }
    }

    fun decryptImageFromFile(path: String): ByteArray {
        val imageFile = File(path)
        require(imageFile.exists()) { "this file is not exist" }
        val imageBytes = imageFile.readBytes()
        return cypher.decrypt(imageBytes)
    }

}