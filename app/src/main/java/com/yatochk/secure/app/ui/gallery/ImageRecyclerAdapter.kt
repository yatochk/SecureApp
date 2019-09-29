package com.yatochk.secure.app.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yatochk.secure.app.R
import kotlinx.android.synthetic.main.image_item.view.*
import java.io.File

class ImageRecyclerAdapter :
    RecyclerView.Adapter<ImageViewHolder>() {

    private val items = ArrayList<File>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageViewHolder = ImageViewHolder(p0)

    override fun onBindViewHolder(p0: ImageViewHolder, p1: Int) {
        p0.bind(items[p1], object : ImageViewHolder.ItemClickListener {
            override fun longClick() {
            }

            override fun click() {
            }
        })
    }

    fun updateImage(videos: List<File>) {
        items.clear()
        items.addAll(videos)
        notifyDataSetChanged()
    }
}

class ImageViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
    ) {

    private val image = itemView.gallery_image

    fun bind(file: File, selectedListener: ItemClickListener) {
        with(itemView) {
            Glide.with(context)
                .load(file)
                .into(image)

            setOnClickListener {
                selectedListener.click()
            }
            setOnLongClickListener {
                selectedListener.longClick()
                true
            }
        }
    }

    interface ItemClickListener {
        fun click()
        fun longClick()
    }
}