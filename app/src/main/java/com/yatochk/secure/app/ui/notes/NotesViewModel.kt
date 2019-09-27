package com.yatochk.secure.app.ui.notes

import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.model.database.dao.NotesDao
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val notesDao: NotesDao
) : ViewModel() {

    val notes = notesDao.getNotes()

}