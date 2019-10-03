package com.yatochk.secure.app.ui.albums

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.main.ErrorType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val imageSecureController: ImageSecureController,
    private val imagesDao: ImagesDao
) : ViewModel() {

    private val mediatorImages = MediatorLiveData<List<Pair<Image, Bitmap>>>()
    val images: LiveData<List<Pair<Image, Bitmap>>> = mediatorImages

    private val mutableShowError = LiveEvent<ErrorType>()
    val showError: LiveData<ErrorType> = mutableShowError

    private val compositeDisposable = CompositeDisposable()

    private fun decryptImage(images: List<Image>) {
        compositeDisposable.add(
            Observable.fromIterable(images)
                .subscribeOn(Schedulers.io())
                .map {
                    val decryptedBytes = imageSecureController.decryptImage(it)
                    val bitmap = BitmapFactory.decodeByteArray(
                        decryptedBytes,
                        0,
                        decryptedBytes.size
                    )
                    Pair(it, bitmap)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { newImage ->
                        val newImages = mutableListOf<Pair<Image, Bitmap>>().apply {
                            mediatorImages.value?.also { addAll(it) }
                            add(newImage)
                        }
                        mediatorImages.value = newImages
                    },
                    {
                        Log.e("On encrypting images", it.localizedMessage, it)
                        mutableShowError.value = ErrorType.ENCRYPT_IMAGE
                    }
                )
        )
    }

    fun setAlbum(albumName: String) {
        mediatorImages.addSource(imagesDao.getImages(albumName)) { images ->
            decryptImage(images)
        }
    }

}