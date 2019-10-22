package com.yatochk.secure.app.ui.notes

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.notes.Note
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : BaseActivity() {

    companion object {
        private const val NOTE = "note"

        fun intent(context: Context, note: Note) =
            Intent(context, NoteActivity::class.java).apply {
                putExtra(NOTE, note)
            }
    }

    private val viewModel: EditNoteViewModel by viewModels { viewModelFactory }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        (intent.getSerializableExtra(NOTE) as? Note)?.also {
            viewModel.initNote(it)
        }
        button_save_note.setOnClickListener {
            viewModel.save(
                text_note_title.text.toString(),
                text_note_body.text.toString()
            )
        }
        button_delete_note.setOnClickListener {
            viewModel.delete()
        }
        observers()
    }

    private fun observers() {
        viewModel.note.observe(this) {
            text_note_title.setText(it.title)
            text_note_body.setText(it.body)
        }
        viewModel.animationFinish.observe(this) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
        viewModel.finish.observe(this) {
            finish()
        }
    }

}
