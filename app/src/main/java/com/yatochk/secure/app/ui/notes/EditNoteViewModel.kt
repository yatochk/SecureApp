package com.yatochk.secure.app.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.database.dao.NotesDao
import com.yatochk.secure.app.model.notes.Note
import com.yatochk.secure.app.utils.postEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditNoteViewModel @Inject constructor(
    private val notesDao: NotesDao
) : ViewModel() {

    private val mutableNote = MutableLiveData<Note>()
    val note: LiveData<Note> = mutableNote

    private val eventFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = eventFinish

    fun initNote(note: Note) {
        mutableNote.value = note
    }

    fun save(title: String, body: String) {
        viewModelScope.launch {
            val updated = note.value!!.apply {
                this.title = title
                this.body = body
            }
            notesDao.updateNote(updated)
            eventFinish.postEvent()
        }
    }

    fun delete() {
        viewModelScope.launch {
            notesDao.deleteNote(note.value!!)
            eventFinish.postEvent()
        }
    }

}