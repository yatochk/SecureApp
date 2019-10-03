package com.yatochk.secure.app.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.model.database.SecureDatabase.Companion.DATABASE_VERSION
import com.yatochk.secure.app.model.database.dao.ContactDao
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.database.dao.NotesDao
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.notes.Note

@Database(
    entities = [
        Contact::class,
        Note::class,
        Image::class
    ],
    version = DATABASE_VERSION
)
abstract class SecureDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "secureDatabase"
        const val DATABASE_VERSION = 1
    }

    abstract val contactDao: ContactDao
    abstract val notesDao: NotesDao
    abstract val imagesDao: ImagesDao

}