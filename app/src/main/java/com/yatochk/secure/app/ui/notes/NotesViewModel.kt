package com.yatochk.secure.app.ui.notes

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.database.dao.NotesDao
import com.yatochk.secure.app.model.notes.Note
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val notesDao: NotesDao
) : ViewModel() {

    val notes = notesDao.getNotes()

    private val eventOpenNote = LiveEvent<Pair<Note, View>>()
    val openNote: LiveData<Pair<Note, View>> = eventOpenNote

    private val mutableShowNewNote = MutableLiveData<Boolean>()
    val showNewNote: LiveData<Boolean> = mutableShowNewNote

    fun clickNote(note: Note, card: View) {
        eventOpenNote.value = Pair(note, card)
    }

    fun addNote() {
        mutableShowNewNote.value = true
    }

}