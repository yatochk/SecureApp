package com.yatochk.secure.app.ui.calculator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import com.yatochk.secure.app.R
import kotlinx.android.synthetic.main.view_keyboard.view.*
import net.objecthunter.exp4j.ExpressionBuilder


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
        button_delete.setOnClickListener {
            onKeyClick(Key.KEY_DELETE)
        }
        button_delete.setOnLongClickListener {
            onKeyClick(Key.KEY_LONG_DELETE)
            return@setOnLongClickListener true
        }
        button_division.setOnClickListener {
            onKeyClick(Key.KEY_DIVIDE)
        }
        button_plus.setOnClickListener {
            onKeyClick(Key.KEY_PLUS)
        }
        button_minus.setOnClickListener {
            onKeyClick(Key.KEY_MINUS)
        }
        button_multiplication.setOnClickListener {
            onKeyClick(Key.KEY_MULTIPLE)
        }
        button_equals.setOnClickListener {
            onKeyClick(Key.KEY_EQUALS)
        }
    }

    private var listener: ((Key) -> Unit)? = null

    fun setKeysListener(listener: (Key) -> Unit) {
        this.listener = listener
    }

    private fun onKeyClick(key: Key) {
        listener?.invoke(key)
    }

    fun showSnack() {
        Snackbar.make(this, R.string.enter_code, Snackbar.LENGTH_LONG).show()
    }
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
    KEY_EQUALS,
    KEY_DELETE,
    KEY_LONG_DELETE,
    KEY_PLUS,
    KEY_MULTIPLE,
    KEY_DIVIDE,
    KEY_MINUS;

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
            KEY_PLUS -> "+"
            KEY_MULTIPLE -> "*"//"×"
            KEY_DIVIDE -> "/"//"÷"
            KEY_MINUS -> "-"
            else -> ""
        }
    }

    fun makeEquals(expression: String?): String {
        return try {
            val res = ExpressionBuilder(expression).build().evaluate()
            if (res % 1 == 0.0)
                res.toInt().toString()
            else
                res.toString()
        } catch (ex: ArithmeticException) {
            ""
        } catch (ex: NumberFormatException) {
            ""
        }
    }

    fun isOperation(key: String): Boolean {
        return when (key) {
            KEY_PLUS.toString(), KEY_MULTIPLE.toString(), KEY_DIVIDE.toString(), KEY_MINUS.toString() -> true
            else -> false
        }
    }

    fun isNumber(): Boolean {
        return when (this) {
            KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9, KEY_0 -> true
            else -> false
        }
    }


}
