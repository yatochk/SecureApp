package com.yatochk.secure.app.ui.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.images.ImageSecureController
import javax.inject.Inject

class ImageVIewModel @Inject constructor(
    private val imageSecureController: ImageSecureController
) : ViewModel() {

    private lateinit var imagePath: String

    private val mediatorImage = MutableLiveData<Bitmap>()
    val image: LiveData<Bitmap> = mediatorImage

    private val mutableClose = LiveEvent<Void>()
    val closeWithDelete: LiveData<Void> = mutableClose

    fun initImagePath(path: String) {
        imagePath = path
        val decryptedBytes = imageSecureController.decryptImage(path)
        mediatorImage.value = BitmapFactory.decodeByteArray(
            decryptedBytes,
            0,
            decryptedBytes.size
        )
    }

    fun onDelete() {
        mutableClose.value = null
    }

    fun onRename() {

    }

    fun onToGallery() {
        mutableClose.value = null
    }

}