package com.yatochk.secure.app.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import javax.inject.Inject

class ContactMenuViewModel @Inject constructor() : ViewModel() {

    private val mutableOpenContacts = LiveEvent<Void>()
    val openContacts: LiveData<Void> = mutableOpenContacts

    private val mutableOpenCreateContact = LiveEvent<Void>()
    val openCreateContact: LiveData<Void> = mutableOpenCreateContact

    fun clickImportContact() {
        mutableOpenContacts.value = null
    }

    fun clickNewContact() {
        mutableOpenCreateContact.value = null
    }

}