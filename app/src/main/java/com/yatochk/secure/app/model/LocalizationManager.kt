package com.yatochk.secure.app.model

import android.content.Context
import com.yatochk.secure.app.R
import com.yatochk.secure.app.ui.main.ContactErrorType
import com.yatochk.secure.app.ui.main.MediaErrorType
import javax.inject.Inject

class LocalizationManager @Inject constructor(
    private val context: Context
) {

    fun getErrorString(errorType: MediaErrorType) =
        context.getString(
            when (errorType) {
                MediaErrorType.ADD_PHOTO -> {
                    R.string.error_photo
                }
                MediaErrorType.ADD_IMAGE -> {
                    R.string.error_gallery
                }
                MediaErrorType.ENCRYPT_MEDIA -> {
                    R.string.error_encrypt_image
                }
                MediaErrorType.DELETE_IMAGE -> {
                    R.string.error_delete_image
                }
                MediaErrorType.TO_GALLERY -> {
                    R.string.error_to_gallery
                }
                MediaErrorType.TO_ALBUM -> {
                    R.string.error_to_album
                }
            }
        )

    fun getErrorString(errorType: ContactErrorType) =
        context.getString(
            when (errorType) {
                ContactErrorType.DELETE_CONTACT -> {
                    R.string.error_delete_contact
                }
                ContactErrorType.UPDATE_CONTACT -> {
                    R.string.error_update_contact
                }
            }
        )

}