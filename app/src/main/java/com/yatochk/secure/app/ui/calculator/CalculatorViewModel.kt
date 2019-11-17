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

    var isDot = false


    fun inputKey(key: Key) {
        contentAccessManager.setAccessKey("32-32+32")
        if (!key.isNumber()) {
            if (key == Key.KEY_DELETE)
                with(mutableDisplayResult) {
                    if (!value.isNullOrEmpty())
                        value = value?.removeLast()
                }
            if (key == Key.KEY_LONG_DELETE)
                with(mutableDisplayResult) {
                    value = ""
                }
            if (key == Key.KEY_EQUALS)
                with(mutableDisplayResult) {
                    if (!value.isNullOrBlank() && !key.isOperation(value!![value!!.length - 1].toString())) {
                        value = Key.KEY_EQUALS.makeEquals(value)
                        isDot = value?.contains(".")!!
                    }
                }
            if (key == Key.KEY_DOT)
                with(mutableDisplayResult) {
                    if ((!value.isNullOrBlank() && (!value?.contains(".")!!) || !isDot)) {
                        keyProcessed(key)
                        isDot = true
                    }
                }
            else
                with(mutableDisplayResult) {
                    if (!value.isNullOrBlank() && !key.isOperation(value!![value!!.length - 1].toString())) {
                        keyProcessed(key)
                        isDot = false
                    }
                }
        } else
            keyProcessed(key)
    }


    private fun keyProcessed(key: Key) {
        with(mutableDisplayResult) {
            if (value.isNullOrBlank())
                value = key.toString()
            else
                value += key.toString()
            if (contentAccessManager.checkAccessKey(value.toString()))
                mutableOpenContent.value = null
        }
    }


}