package com.yatochk.secure.app.model.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String,
    var body: String
) : Serializable