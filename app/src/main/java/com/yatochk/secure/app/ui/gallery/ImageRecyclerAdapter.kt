package com.yatochk.secure.app.ui.gallery

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yatochk.secure.app.R
import com.yatochk.secure.app.model.images.Image
import kotlinx.android.synthetic.main.image_item.view.*

class ImageRecyclerAdapter(
    private val itemClickListener: (Image, ImageView) -> Unit
) : ListAdapter<Pair<Image, Bitmap>, ImageViewHolder>(BitmapDiffUtils()) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageViewHolder = ImageViewHolder(p0)

    override fun onBindViewHolder(p0: ImageViewHolder, p1: Int) {
        p0.bind(getItem(p1), object : ImageViewHolder.ItemClickListener {
            override fun longClick(imageView: ImageView) {
            }

            override fun click(imageView: ImageView) {
                itemClickListener(getItem(p1).first, imageView)
            }
        })
    }
}

class ImageViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
    ) {

    private val image = itemView.gallery_image

    fun bind(pair: Pair<Image, Bitmap>, clickListener: ItemClickListener) {
        with(itemView) {
            Glide.with(context)
                .load(pair.second)
                .into(image)

            setOnClickListener {
                clickListener.click(image)
            }
            setOnLongClickListener {
                clickListener.longClick(image)
                true
            }
        }
    }

    interface ItemClickListener {
        fun click(imageView: ImageView)
        fun longClick(imageView: ImageView)
    }
}

class BitmapDiffUtils : DiffUtil.ItemCallback<Pair<Image, Bitmap>>() {
    override fun areItemsTheSame(
        oldItem: Pair<Image, Bitmap>,
        newItem: Pair<Image, Bitmap>
    ): Boolean =
        oldItem.first == newItem.first

    override fun areContentsTheSame(
        oldItem: Pair<Image, Bitmap>,
        newItem: Pair<Image, Bitmap>
    ): Boolean =
        oldItem.second.rowBytes == newItem.second.rowBytes

}
