package com.yatochk.secure.app.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.utils.postEvent
import javax.inject.Inject

class GalleryMenuViewModel @Inject constructor() : ViewModel() {

    private val mutableOpenCamera = LiveEvent<Void>()
    val openCamera: LiveData<Void> = mutableOpenCamera

    private val mutableOpenVideoCamera = LiveEvent<Void>()
    val openVideoCamera: LiveData<Void> = mutableOpenVideoCamera

    private val mutableOpenGallery = LiveEvent<Void>()
    val openGallery: LiveData<Void> = mutableOpenGallery

    private val mutableOpenVideoGallery = LiveEvent<Void>()
    val openVideoGallery: LiveData<Void> = mutableOpenVideoGallery

    private val eventTypePicker = LiveEvent<Void>()
    val openTypePicker: LiveData<Void> = eventTypePicker

    fun clickPhoto() {
        eventTypePicker.postEvent()
    }

    fun clickGallery() {
        eventTypePicker.postEvent()
    }

}