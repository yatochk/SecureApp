package com.yatochk.secure.app.utils

private const val DOT = "."

fun String.removeLast() =
    substring(0, lastIndex)

fun String.isVideoPath() =
    contains(CAMERA_VIDEO_FORMAT) || contains(REGULAR_VIDEO_FORMAT)

fun String.insertPostfix(postfix: String): String {
    val lastDot = lastIndexOf(DOT)
    val s = substring(0, lastDot) + postfix
    return s + substring(lastDot)
}

fun String.changeFormat(format: String): String {
    val withoutFormat = substring(0, lastIndexOf(DOT))
    return withoutFormat + format
}