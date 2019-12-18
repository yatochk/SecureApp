package com.yatochk.secure.app.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yatochk.secure.app.R
import com.yatochk.secure.app.model.images.Album
import com.yatochk.secure.app.utils.contentEqualsNullable
import kotlinx.android.synthetic.main.album_item.view.*

class AlbumRecyclerAdapter(
    private val itemClickListener: (String, View) -> Unit
) : ListAdapter<Album, AlbumViewHolder>(DiffAlbum()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder =
        AlbumViewHolder(parent)

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        getItem(position).also { album ->
            holder.bind(album) {
                itemClickListener(album.name, it)
            }
        }
    }

}

class DiffAlbum : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean =
        oldItem.name == newItem.name && oldItem.preview?.contentEqualsNullable(newItem.preview) == true

}

class AlbumViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
    ) {

    private val textName = itemView.text_album_name
    private val imageView = itemView.gallery_album

    fun bind(album: Album, clickListener: (View) -> Unit) {
        itemView.setOnClickListener { clickListener(textName) }
        textName.text = album.name
        album.preview?.also {
            Glide.with(itemView.context)
                .load(it)
                .into(imageView)
        }
    }

}