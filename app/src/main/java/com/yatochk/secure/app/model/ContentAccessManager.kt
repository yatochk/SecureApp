package com.yatochk.secure.app.model

import android.content.SharedPreferences
import android.text.TextUtils
import javax.inject.Inject

class ContentAccessManager @Inject constructor(
    private val cypher: Cypher,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val ACCESS_KEY = "access_to_content"
    }

    fun setAccessKey(key: String) {
        sharedPreferences.edit()
            .putString(ACCESS_KEY, String(cypher.encrypt(key.toByteArray())))
            .apply()
    }

    private val accessKey: String
        get() {
            sharedPreferences.getString(ACCESS_KEY, null)?.toByteArray()?.also {
                return String(cypher.decrypt(it))
            }
            return ""
        }

    private val isKeyExist: Boolean
        get() = accessKey.isNotBlank()

    fun checkAccessKey(key: String) =
        isKeyExist && key == accessKey

    fun isAuthorized(): Boolean =
        !TextUtils.isEmpty(sharedPreferences.getString(ACCESS_KEY, null))
}