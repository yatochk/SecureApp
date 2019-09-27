package com.yatochk.secure.app.model.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey
    var id: Int,
    var title: String,
    var body: String
)