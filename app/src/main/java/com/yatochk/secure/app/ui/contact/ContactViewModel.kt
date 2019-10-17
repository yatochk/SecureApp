package com.yatochk.secure.app.ui.contact

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.model.database.dao.ContactDao
import com.yatochk.secure.app.utils.postEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {

    val contacts = contactDao.getContacts()

    private val eventOpenContact = LiveEvent<Pair<View, Contact>>()
    val openContact: LiveData<Pair<View, Contact>> = eventOpenContact

    private val eventHideNewContact = LiveEvent<Void>()
    val hideNewContact: LiveData<Void> = eventHideNewContact

    fun clickContact(view: View, contact: Contact) {
        eventOpenContact.value = Pair(view, contact)
    }

    fun receivedContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.addContact(contact)
        }
    }

    fun saveContact(name: String, number: String) {
        viewModelScope.launch {
            contactDao.addContact(Contact(number, name))
            eventHideNewContact.postEvent()
        }
    }

    fun cancelNewContact() {
        eventHideNewContact.postEvent()
    }

}