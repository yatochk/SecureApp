package com.yatochk.secure.app.ui.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.model.images.ImageSecureController
import javax.inject.Inject

class ImageVIewModel @Inject constructor(
    private val imageSecureController: ImageSecureController
) : ViewModel() {

    private val mediatorImage = MutableLiveData<Bitmap>()
    val image: LiveData<Bitmap> = mediatorImage

    fun initImagePath(path: String) {
        val decryptedBytes = imageSecureController.decryptImage(path)
        mediatorImage.value = BitmapFactory.decodeByteArray(
            decryptedBytes,
            0,
            decryptedBytes.size
        )
    }
}