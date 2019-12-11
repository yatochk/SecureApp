package com.yatochk.secure.app.utils

fun String.removeLast() =
    substring(0, lastIndex)

fun String.isVideoPath() =
    contains("MPEG_4") || contains("mp4")