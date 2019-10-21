package com.yatochk.secure.app.ui.notes

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.flexbox.FlexboxLayoutManager
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : BaseFragment() {

    private val viewModel: NotesViewModel by viewModels { viewModelFactory }

    override val layoutId = R.layout.fragment_notes

    private lateinit var adapter: NotesRecyclerAdapter

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotesRecyclerAdapter { note, card ->
            viewModel.clickNote(note, card)
        }
        recycler_notes.layoutManager = FlexboxLayoutManager(activity)
        recycler_notes.adapter = adapter
        floating_add_note.setOnClickListener {
            viewModel.addNote()
        }
        observers()
    }

    private fun animateShowNewNote(show: Boolean) {

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
            animateShowNewNote(it)
        }
    }

}