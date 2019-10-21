package com.yatochk.secure.app.ui.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.notes.Note
import com.yatochk.secure.app.ui.BaseActivity

class NoteActivity : BaseActivity() {

    companion object {
        private const val NOTE = "note"

        fun intent(context: Context, note: Note) =
            Intent(context, NoteActivity::class.java).apply {
                putExtra(NOTE, note)
            }
    }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
    }

}
