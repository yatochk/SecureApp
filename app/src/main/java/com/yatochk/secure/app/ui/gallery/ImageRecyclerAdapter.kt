package com.yatochk.secure.app.ui.gallery

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yatochk.secure.app.R
import kotlinx.android.synthetic.main.image_item.view.*

class ImageRecyclerAdapter
    : ListAdapter<Bitmap, ImageViewHolder>(BitmapDiffUtils()) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageViewHolder = ImageViewHolder(p0)

    override fun onBindViewHolder(p0: ImageViewHolder, p1: Int) {
        p0.bind(getItem(p1), object : ImageViewHolder.ItemClickListener {
            override fun longClick() {
            }

            override fun click() {
            }
        })
    }
}

class ImageViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
    ) {

    private val image = itemView.gallery_image

    fun bind(bitmap: Bitmap, selectedListener: ItemClickListener) {
        with(itemView) {
            Glide.with(context)
                .load(bitmap)
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

class BitmapDiffUtils : DiffUtil.ItemCallback<Bitmap>() {
    override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean =
        oldItem.rowBytes == newItem.rowBytes

    override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean =
        oldItem.rowBytes == newItem.rowBytes

}
