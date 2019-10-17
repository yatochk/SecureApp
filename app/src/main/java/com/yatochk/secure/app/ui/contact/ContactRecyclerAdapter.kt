package com.yatochk.secure.app.ui.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yatochk.secure.app.R
import com.yatochk.secure.app.model.contact.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactRecyclerAdapter(
    private val itemClickListener: (View, Contact) -> Unit
) : ListAdapter<Contact, ContactViewHolder>(ContactDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(parent)

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            itemClickListener(it, getItem(position))
        }
    }

}

class ContactViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
) {

    private val contactName = itemView.text_contact_name
    private val contactNumber = itemView.text_contact_number
    private val card = itemView.card_contact

    fun bind(contact: Contact, itemClickListener: (View) -> Unit) {
        itemView.setOnClickListener {
            itemClickListener(card)
        }
        contactName.text = contact.name
        contactNumber.text = contact.number
    }

}

class ContactDiff : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.name == newItem.name && oldItem.number == newItem.number

}