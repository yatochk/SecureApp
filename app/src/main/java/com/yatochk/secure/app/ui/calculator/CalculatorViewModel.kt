package com.yatochk.secure.app.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.ContentAccessManager
import com.yatochk.secure.app.utils.postEvent
import com.yatochk.secure.app.utils.removeLast
import javax.inject.Inject


class CalculatorViewModel @Inject constructor(
    private val contentAccessManager: ContentAccessManager
) : ViewModel() {

    private val mutableDisplayResult = MutableLiveData<String>()
    val displayResult: LiveData<String> = mutableDisplayResult

    private val mutableOpenContent = MutableLiveData<Void>()
    val openContent: LiveData<Void> = mutableOpenContent

    private val eventOpenDialog = LiveEvent<Void>()
    val openDialog: LiveData<Void> = eventOpenDialog

    private var isDot = false

    fun onViewReady() {
        if (!contentAccessManager.isKeyExist) {
            eventOpenDialog.postEvent()
        }
    }


    fun inputKey(key: Key) {
        if (!key.isNumber()) {
            when (key) {
                Key.KEY_DELETE ->
                    with(mutableDisplayResult) {
                        if (!value.isNullOrEmpty())
                            value = value?.removeLast()
                    }
                Key.KEY_LONG_DELETE ->
                    with(mutableDisplayResult) {
                        value = ""
                    }
                Key.KEY_EQUALS ->
                    with(mutableDisplayResult) {
                        if (!contentAccessManager.isKeyExist) {
                            contentAccessManager.setAccessKey(value.toString())
                            mutableOpenContent.value = null
                            return
                        } else if (contentAccessManager.checkAccessKey(value.toString())) {
                            mutableOpenContent.value = null
                            return
                        }
                        if (!value.isNullOrBlank() && !key.isOperation(value!![value!!.length - 1].toString())) {
                            value = Key.KEY_EQUALS.makeEquals(value)
                            isDot = value?.contains(".")!!
                        }
                    }
                Key.KEY_DOT ->
                    with(mutableDisplayResult) {
                        if ((!value.isNullOrBlank() && (!value?.contains(".")!!) || !isDot)) {
                            keyProcessed(key)
                            isDot = true
                        }
                    }
                else ->
                    with(mutableDisplayResult) {
                        if (!value.isNullOrBlank() && !key.isOperation(value!![value!!.length - 1].toString())) {
                            keyProcessed(key)
                            isDot = false
                        }
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
        }
    }

}