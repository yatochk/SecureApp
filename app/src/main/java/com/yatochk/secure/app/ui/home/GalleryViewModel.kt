package com.yatochk.secure.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import javax.inject.Inject

class GalleryViewModel @Inject constructor() : ViewModel() {

    private val mutableImages = MutableLiveData<List<File>>()
    val images: LiveData<List<File>> = mutableImages

}