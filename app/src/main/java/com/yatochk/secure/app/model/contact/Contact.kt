package com.yatochk.secure.app.model.contact

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Contact(
    @PrimaryKey
    var number: String,
    var name: String
) : Serializable