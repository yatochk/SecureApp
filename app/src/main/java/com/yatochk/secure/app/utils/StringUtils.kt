package com.yatochk.secure.app.utils

fun String.removeLast() =
    substring(0, lastIndex)

fun String.isVideoPath() =
    contains(CAMERA_VIDEO_FORMAT) || contains(REGULAR_VIDEO_FORMAT)