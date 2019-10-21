package com.yatochk.secure.app.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yatochk.secure.app.R
import com.yatochk.secure.app.model.notes.Note
import kotlinx.android.synthetic.main.note_item.view.*

class NotesRecyclerAdapter(
    private val itemClickListener: (Note, View) -> Unit
) : ListAdapter<Note, NoteViewHolder>(NotesDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(parent)

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            itemClickListener(getItem(position), it)
        }
    }

}

class NoteViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
) {

    private val titleText = itemView.item_note_title
    private val bodyText = itemView.item_note_body
    private val cardNote = itemView.card_note

    fun bind(note: Note, listener: (View) -> Unit) {
        itemView.setOnClickListener {
            listener(cardNote)
        }
        titleText.text = note.title
        bodyText.text = note.body
    }

}

class NotesDiffUtils : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
        oldItem.body == newItem.body

}