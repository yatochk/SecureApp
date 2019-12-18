package com.yatochk.secure.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yatochk.secure.app.R
import kotlinx.android.synthetic.main.album_picker_item.view.*

class AlbumsAdapter(
    private val listener: (String) -> Unit
) : ListAdapter<String, AlbumPickerViewHolder>(AlbumPickerDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPickerViewHolder =
        AlbumPickerViewHolder(parent)

    override fun onBindViewHolder(holder: AlbumPickerViewHolder, position: Int) {
        val name = getItem(position)
        holder.bind(name) {
            listener(name)
        }
    }

}

class AlbumPickerDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem


    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

}

class AlbumPickerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.album_picker_item, parent, false)
) {

    private val albumName = itemView.picker_album_name

    fun bind(name: String, clickListener: () -> Unit) {
        albumName.text = name
        itemView.setOnClickListener {
            clickListener()
        }
    }

}