package com.yatochk.secure.app.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snakydesign.livedataextensions.map
import javax.inject.Inject

class BrowserViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val PROTOCOL_PREFIX = "https://"
        private const val START_URL = "duckduckgo.com"
    }

    private val mutableLoadUrl = MutableLiveData<String>().apply {
        value = START_URL
    }
    val loadUrl: LiveData<String> = mutableLoadUrl.map {
        PROTOCOL_PREFIX + it
    }

    private val mutableShowUrl = MutableLiveData<String>().apply {
        value = START_URL
    }
    val showUrl: LiveData<String> = mutableShowUrl

    fun inputUrl(url: String) {
        mutableLoadUrl.value = if (url.isBlank()) START_URL else url
    }

}