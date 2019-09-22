package com.yatochk.secure.app.model.contact

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey
    var number: String,
    var name: String
)