package com.yatochk.secure.app.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.model.ContentAccessManager
import com.yatochk.secure.app.utils.removeLast
import javax.inject.Inject

class CalculatorViewModel @Inject constructor(
    private val contentAccessManager: ContentAccessManager
) : ViewModel() {

    private val mutableDisplayResult = MutableLiveData<String>()
    val displayResult: LiveData<String> = mutableDisplayResult

    private val mutableOpenContent = MutableLiveData<Void>()
    val openContent: LiveData<Void> = mutableOpenContent

    fun inputKey(key: Key) {
        if (key == Key.KEY_DELETE) {
            with(mutableDisplayResult) {
                value = value?.removeLast()
            }
        } else {
            keyProcessed(key)
        }
    }

    private fun keyProcessed(key: Key) {
        with(mutableDisplayResult) {
            if (value.isNullOrBlank()) {
                value = key.toString()
            } else {
                value += key.toString()
            }
            if (value?.length == 3) {
                mutableOpenContent.value = null
            }
        }
    }

}