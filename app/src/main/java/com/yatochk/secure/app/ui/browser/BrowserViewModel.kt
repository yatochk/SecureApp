package com.yatochk.secure.app.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class BrowserViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val START_URL = "www.google.com"
    }

    private val mutableLoadUrl = MutableLiveData<String>().apply {
        value = START_URL
    }

    val loadUrl: LiveData<String> = mutableLoadUrl

}