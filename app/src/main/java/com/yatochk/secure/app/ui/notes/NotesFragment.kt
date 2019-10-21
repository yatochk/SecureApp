package com.yatochk.secure.app.ui.notes

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment
import com.yatochk.secure.app.utils.clear
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : BaseFragment() {

    private val viewModel: NotesViewModel by viewModels { viewModelFactory }

    override val layoutId = R.layout.fragment_notes

    private lateinit var adapter: NotesRecyclerAdapter

    private lateinit var notes: ConstraintSet
    private lateinit var newNote: ConstraintSet

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notes = ConstraintSet()
        notes.clone(root_note)
        newNote = ConstraintSet()
        newNote.clone(activity, R.layout.fragment_new_note)
        adapter = NotesRecyclerAdapter { note, card ->
            viewModel.clickNote(note, card)
        }
        recycler_notes.layoutManager = GridLayoutManager(activity, 2)
        recycler_notes.adapter = adapter
        floating_add_note.setIcon(R.drawable.ic_add_note)
        floating_add_note.setOnClickListener {
            viewModel.addNote()
        }
        button_cancel_note.setOnClickListener {
            viewModel.cancelNewNote()
        }
        button_save_note.setOnClickListener {
            viewModel.saveNote(
                new_note_title.text.toString(),
                new_note_body.text.toString()
            )
        }
        observers()
    }

    private fun animateShowNewNote(showNew: Boolean) {
        TransitionManager.beginDelayedTransition(root_note)
        (if (showNew) newNote else notes).applyTo(root_note)
    }

    private fun observers() {
        viewModel.notes.observe(this) {
            adapter.submitList(it)
        }
        viewModel.openNote.observe(this) {
            val bundle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    it.second,
                    getString(R.string.note_transition)
                ).toBundle()
            } else {
                null
            }
            startActivity(NoteActivity.intent(activity!!, it.first), bundle)
        }
        viewModel.showNewNote.observe(this) {
            new_note_title.clear()
            new_note_body.clear()
            animateShowNewNote(it)
        }
    }

}