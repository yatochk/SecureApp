package com.yatochk.secure.app.utils

import android.view.KeyEvent
import android.widget.EditText

inline fun EditText.onDone(crossinline listener: (String) -> Unit) {
    this.setOnEditorActionListener { _, keycode, keyEvent ->
        if (keyEvent.action == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER) {
            listener(this.text.toString())
            true
        } else {
            false
        }
    }
}