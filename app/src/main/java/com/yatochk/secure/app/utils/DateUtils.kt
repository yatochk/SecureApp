package com.yatochk.secure.app.utils

import java.text.SimpleDateFormat
import java.util.*

private val timeFormat = SimpleDateFormat("yyyy-MM-dd-HH:mm_ss", Locale.ENGLISH)

fun Date.toTimeString(): String =
    timeFormat.format(this)

