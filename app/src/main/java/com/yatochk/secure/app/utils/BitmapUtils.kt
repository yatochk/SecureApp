package com.yatochk.secure.app.utils

import android.graphics.Bitmap
import kotlin.math.min
import kotlin.math.roundToInt

fun Bitmap.scaleDown(
    maxImageSize: Float,
    filter: Boolean
): Bitmap {
    if (maxImageSize > this.width && maxImageSize > this.height)
        return this

    val ratio = min(
        maxImageSize / this.width,
        maxImageSize / this.height
    )
    val width = (ratio * this.width).roundToInt()
    val height = (ratio * this.height).roundToInt()

    return Bitmap.createScaledBitmap(
        this, width,
        height, filter
    )
}