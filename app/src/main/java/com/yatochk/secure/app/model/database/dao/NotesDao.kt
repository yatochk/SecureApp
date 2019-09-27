package com.yatochk.secure.app.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yatochk.secure.app.model.notes.Note

@Dao
interface NotesDao {

    @Query("SELECT * FROM Note")
    fun getNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

}