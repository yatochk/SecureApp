package com.yatochk.secure.app.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

class CalculatorViewModel : ViewModel() {

    private val mutableDisplayResult = MutableLiveData<String>()
    val displayResult: LiveData<String> = mutableDisplayResult

    private val eventOpenContent = LiveEvent<Void>()
    val openContent: LiveData<Void> = eventOpenContent

    fun inputKey(key: KeyboardView.Key) {
        with(mutableDisplayResult) {
            if (key == KeyboardView.Key.KEY_DELETE) {
                value = value?.substring(0, value!!.lastIndex)
            } else {
                value += key
            }
        }
    }

}