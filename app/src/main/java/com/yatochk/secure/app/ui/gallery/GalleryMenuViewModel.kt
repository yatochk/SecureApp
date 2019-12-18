package com.yatochk.secure.app.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.utils.postEvent
import javax.inject.Inject

class GalleryMenuViewModel @Inject constructor() : ViewModel() {

    private var fromGallery = false

    private val eventOpenCamera = LiveEvent<Void>()
    val openCamera: LiveData<Void> = eventOpenCamera

    private val eventOpenVideoCamera = LiveEvent<Void>()
    val openVideoCamera: LiveData<Void> = eventOpenVideoCamera

    private val eventOpenGallery = LiveEvent<Void>()
    val openGallery: LiveData<Void> = eventOpenGallery

    private val eventOpenVideoGallery = LiveEvent<Void>()
    val openVideoGallery: LiveData<Void> = eventOpenVideoGallery

    private val eventOpenTypePicker = LiveEvent<Void>()
    val openTypePicker: LiveData<Void> = eventOpenTypePicker

    private val eventHideTypePicker = LiveEvent<Void>()
    val hideTypePicker: LiveData<Void> = eventHideTypePicker

    fun clickPhoto() {
        fromGallery = false
        eventOpenTypePicker.postEvent()
    }

    fun clickGallery() {
        fromGallery = true
        eventOpenTypePicker.postEvent()
    }

    fun onPickerCancel() {
        eventHideTypePicker.postEvent()
    }

    fun onPickVideo() {
        eventHideTypePicker.postEvent()
        if (fromGallery) {
            eventOpenVideoGallery.postEvent()
        } else {
            eventOpenVideoCamera.postEvent()
        }
    }

    fun onPickImage() {
        eventHideTypePicker.postEvent()
        if (fromGallery) {
            eventOpenGallery.postEvent()
        } else {
            eventOpenCamera.postEvent()
        }
    }

}