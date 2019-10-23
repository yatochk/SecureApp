package com.yatochk.secure.app.ui.calculator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.yatochk.secure.app.R
import kotlinx.android.synthetic.main.view_keyboard.view.*

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_keyboard, this)
        button_0.setOnClickListener {
            onKeyClick(Key.KEY_0)
        }
        button_1.setOnClickListener {
            onKeyClick(Key.KEY_1)
        }
        button_2.setOnClickListener {
            onKeyClick(Key.KEY_2)
        }
        button_3.setOnClickListener {
            onKeyClick(Key.KEY_3)
        }
        button_4.setOnClickListener {
            onKeyClick(Key.KEY_4)
        }
        button_5.setOnClickListener {
            onKeyClick(Key.KEY_5)
        }
        button_6.setOnClickListener {
            onKeyClick(Key.KEY_6)
        }
        button_7.setOnClickListener {
            onKeyClick(Key.KEY_7)
        }
        button_8.setOnClickListener {
            onKeyClick(Key.KEY_8)
        }
        button_9.setOnClickListener {
            onKeyClick(Key.KEY_9)
        }
        button_dot.setOnClickListener {
            onKeyClick(Key.KEY_DOT)
        }
        button_equals.setOnClickListener {
            onKeyClick(Key.KEY_EQUALS)
        }
    }

    private var listener: ((Key) -> Unit)? = null

    fun setKeysListener(listener: (Key) -> Unit) {
        this.listener = listener
    }

    lateinit var displayView: TextView

    private fun onKeyClick(key: Key) {
        displayView.append(key.toString())
        listener?.invoke(key)
    }

    enum class Key {
        KEY_1,
        KEY_2,
        KEY_3,
        KEY_4,
        KEY_5,
        KEY_6,
        KEY_7,
        KEY_8,
        KEY_9,
        KEY_0,
        KEY_DOT,
        KEY_EQUALS;

        override fun toString(): String {
            return when (this) {
                KEY_0 -> "0"
                KEY_1 -> "1"
                KEY_2 -> "2"
                KEY_3 -> "3"
                KEY_4 -> "4"
                KEY_5 -> "5"
                KEY_6 -> "6"
                KEY_7 -> "7"
                KEY_8 -> "8"
                KEY_9 -> "9"
                KEY_DOT -> "."
                KEY_EQUALS -> ""
            }
        }

    }

}