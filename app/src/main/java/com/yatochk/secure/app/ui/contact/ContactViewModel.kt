package com.yatochk.secure.app.ui.contact

import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.model.database.dao.ContactDao
import javax.inject.Inject

class ContactViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {

    val contacts = contactDao.getContacts()

}