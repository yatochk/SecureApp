package com.yatochk.secure.app.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yatochk.secure.app.R
import com.yatochk.secure.app.model.images.Album
import kotlinx.android.synthetic.main.album_item.view.*

class AlbumRecyclerAdapter :
    ListAdapter<Album, AlbumViewHolder>(DiffAlbum()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder =
        AlbumViewHolder(parent)

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) =
        holder.bind(getItem(position))

}

class DiffAlbum : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean =
        oldItem.name == newItem.name && oldItem.preview.rowBytes == newItem.preview.rowBytes

}

class AlbumViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
    ) {

    private val textName = itemView.text_album_name
    private val imageView = itemView.gallery_album

    fun bind(album: Album) {
        textName.text = album.name
        Glide.with(itemView.context)
            .load(album.preview)
            .into(imageView)
    }

}