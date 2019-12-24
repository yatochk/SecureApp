package com.yatochk.secure.app.utils

fun String.removeLast() =
    substring(0, lastIndex)

fun String.isVideoPath() =
    contains(CAMERA_VIDEO_FORMAT) || contains(REGULAR_VIDEO_FORMAT)

fun String.insertPostfix(postfix: String): String {
    val lastDot = lastIndexOf(".")
    val s = substring(0, lastDot) + postfix
    return s + substring(lastDot)
}