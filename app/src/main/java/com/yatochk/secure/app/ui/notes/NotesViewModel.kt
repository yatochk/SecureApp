package com.yatochk.secure.app.ui.notes

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.database.dao.NotesDao
import com.yatochk.secure.app.model.notes.Note
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val notesDao: NotesDao
) : ViewModel() {

    val notes = notesDao.getNotes()

    private val eventOpenNote = LiveEvent<Pair<Note, View>>()
    val openNote: LiveData<Pair<Note, View>> = eventOpenNote

    private val mutableShowNewNote = MutableLiveData<Boolean>()
    val showNewNote: LiveData<Boolean> = mutableShowNewNote

    private val notesExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(NotesViewModel::class.java.simpleName, throwable.localizedMessage, throwable)
    }

    fun clickNote(note: Note, card: View) {
        eventOpenNote.value = Pair(note, card)
    }

    fun addNote() {
        mutableShowNewNote.value = true
    }

    fun saveNote(title: String, body: String) {
        viewModelScope.launch(notesExceptionHandler) {
            notesDao.addNote(Note(title = title, body = body))
            mutableShowNewNote.value = false
        }
    }

    fun cancelNewNote() {
        mutableShowNewNote.value = false
    }

}