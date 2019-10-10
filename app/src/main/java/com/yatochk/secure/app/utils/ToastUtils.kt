package com.yatochk.secure.app.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.yatochk.secure.app.R
import es.dmoral.toasty.Toasty

fun showErrorToast(context: Context, msg: String) {
    Toasty.custom(
        context,
        msg,
        null,
        ContextCompat.getColor(context, R.color.error),
        ContextCompat.getColor(context, R.color.titleTextColor),
        Toast.LENGTH_LONG,
        false,
        true
    ).apply {
        setMargin(0f, 0.07f)
    }.show()
}