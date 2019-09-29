package com.yatochk.secure.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

class MainViewModel : ViewModel() {

    private val mutableOpenCamera = LiveEvent<Void>()
    val openCamera: LiveData<Void> = mutableOpenCamera

    private val mutableOpenGallery = LiveEvent<Void>()
    val openGallery: LiveData<Void> = mutableOpenGallery

    fun clickPhoto() {
        mutableOpenCamera.value = null
    }

    fun clickGallery() {
        mutableOpenGallery.value = null
    }

}