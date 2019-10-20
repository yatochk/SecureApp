package com.yatochk.secure.app.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
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
        if (it.contains(PROTOCOL_PREFIX))
            it
        else
            PROTOCOL_PREFIX + it
    }

    private val eventEditMode = LiveEvent<Boolean>()
    val editMode: LiveData<Boolean> = eventEditMode

    private val mutableShowUrl = MutableLiveData<String>().apply {
        value = START_URL
    }
    val showUrl: LiveData<String> = mutableShowUrl.map {
        if (it.contains(PROTOCOL_PREFIX))
            it.substringAfter(PROTOCOL_PREFIX)
        else
            it
    }

    fun startLoadUrl(url: String) {
        mutableShowUrl.value = url
    }

    fun editFocused(isFocused: Boolean) {
        eventEditMode.value = isFocused
    }

    fun inputUrl(url: String) {
        mutableLoadUrl.value = if (url.isBlank()) START_URL else url
        eventEditMode.value = false
    }

    fun clickHome() {
        mutableLoadUrl.value = START_URL
    }

}