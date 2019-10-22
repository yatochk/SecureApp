package com.yatochk.secure.app.ui.calculator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.yatochk.secure.app.R

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_keyboard, this)
    }

    private var listener: ((Int) -> Unit)? = null

    fun setKeysListener(listener: (Int) -> Unit) {
        this.listener = listener
    }

    private fun onKeyClick(key: Int) {
        listener?.invoke(key)
    }


}