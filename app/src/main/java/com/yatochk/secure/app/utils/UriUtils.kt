package com.yatochk.secure.app.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun Uri.toPath(context: Context): String {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(
        this,
        filePathColumn,
        null,
        null,
        null
    ) ?: return ""
    cursor.moveToFirst()
    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
    val picturePath = cursor.getString(columnIndex)
    cursor.close()
    return picturePath
}

