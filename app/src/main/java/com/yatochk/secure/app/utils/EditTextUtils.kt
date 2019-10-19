package com.yatochk.secure.app.utils

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


inline fun EditText.onDone(crossinline listener: (String) -> Unit) {
    setOnEditorActionListener { _, keycode, keyEvent ->
        if (keycode == EditorInfo.IME_ACTION_DONE
            || keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER
        ) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
                it.hideSoftInputFromWindow(windowToken, 0)
            }
            listener(text.toString())
            true
        } else {
            false
        }
    }
}

inline fun EditText.onSearch(crossinline listener: (String) -> Unit) {
    setOnEditorActionListener { _, keycode, keyEvent ->
        if (keycode == EditorInfo.IME_ACTION_SEARCH
            || keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER
        ) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
                it.hideSoftInputFromWindow(windowToken, 0)
            }
            listener(text.toString())
            true
        } else {
            false
        }
    }
}

fun EditText.clear() =
    setText("")
