package com.yatochk.secure.app.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yatochk.secure.app.model.notes.Note

@Dao
interface NotesDao {

    @Query("SELECT * FROM Note")
    fun getNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

}