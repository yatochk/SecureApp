package com.yatochk.secure.app.ui.contact

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.model.database.dao.ContactDao
import javax.inject.Inject

class ContactViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {

    val contacts = contactDao.getContacts()

    private val eventOpenContact = LiveEvent<Pair<View, Contact>>()
    val openContact: LiveData<Pair<View, Contact>> = eventOpenContact

    fun clickContact(view: View, contact: Contact) {
        eventOpenContact.value = Pair(view, contact)
    }

}