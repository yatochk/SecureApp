package com.yatochk.secure.app.model.images

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Image(
    @PrimaryKey
    var securePath: String,
    var regularPath: String,
    var album: String
) : Serializable