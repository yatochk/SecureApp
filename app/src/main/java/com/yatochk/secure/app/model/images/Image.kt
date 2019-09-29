package com.yatochk.secure.app.model.images

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    @PrimaryKey
    var path: String,
    var album: String
)