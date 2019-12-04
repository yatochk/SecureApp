package com.yatochk.secure.app.model

import android.content.Context
import com.yatochk.secure.app.R
import com.yatochk.secure.app.ui.main.ContactErrorType
import com.yatochk.secure.app.ui.main.ImageErrorType
import javax.inject.Inject

class LocalizationManager @Inject constructor(
    private val context: Context
) {

    fun getErrorString(errorType: ImageErrorType) =
        when (errorType) {
            ImageErrorType.ADD_PHOTO -> {
                context.getString(R.string.error_photo)
            }
            ImageErrorType.ADD_IMAGE -> {
                context.getString(R.string.error_gallery)
            }
            ImageErrorType.ENCRYPT_IMAGE -> {
                context.getString(R.string.error_encrypt_image)
            }
            ImageErrorType.DELETE_IMAGE -> {
                context.getString(R.string.error_delete_image)
            }
            ImageErrorType.TO_GALLERY -> {
                context.getString(R.string.error_to_gallery)
            }
        }

    fun getErrorString(errorType: ContactErrorType) =
        when (errorType) {
            ContactErrorType.DELETE_CONTACT -> {
                context.getString(R.string.error_delete_contact)
            }
            ContactErrorType.UPDATE_CONTACT -> {
                context.getString(R.string.error_update_contact)
            }
        }

}