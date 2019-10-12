package com.yatochk.secure.app.utils

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import com.yatochk.secure.app.model.contact.Contact

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

fun Uri.toContact(context: Context): Contact? {
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    val cursor = context.contentResolver.query(
        this, projection,
        null, null, null
    )

    if (cursor != null && cursor.moveToFirst()) {
        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val name = cursor.getString(nameIndex)
        val number = cursor.getString(numberIndex)
        cursor.close()
        return Contact(number, name)
    }

    return null
}

