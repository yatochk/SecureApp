package com.yatochk.secure.app.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.LocalizationManager
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.model.database.dao.ContactDao
import com.yatochk.secure.app.ui.main.ContactErrorType
import com.yatochk.secure.app.utils.postEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditContactViewModel @Inject constructor(
    private val contactDao: ContactDao,
    private val localizationManager: LocalizationManager
) : ViewModel() {

    private val mutableContact = MutableLiveData<Contact>()
    val contact: LiveData<Contact> = mutableContact

    private val eventFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = eventFinish

    private val eventCall = LiveEvent<String>()
    val call: LiveData<String> = eventCall

    private val eventError = LiveEvent<String>()
    val error: LiveData<String> = eventError

    fun initContact(contact: Contact) {
        mutableContact.value = contact
    }

    fun call() {
        eventCall.value = contact.value!!.number
    }

    fun editName(name: String) {
        val updated = contact.value!!.apply {
            this.name = name
        }
        updateContact(updated)
    }

    fun editNumber(number: String) {
        val updated = contact.value!!.apply {
            this.number = number
        }
        updateContact(updated)
    }

    private fun updateContact(updated: Contact) =
        viewModelScope.launch(CoroutineExceptionHandler { _, _ ->
            eventError.value = localizationManager.getErrorString(ContactErrorType.UPDATE_CONTACT)
        }) {
            contactDao.updateContact(updated)
        }

    fun delete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, _ ->
            eventError.value = localizationManager.getErrorString(ContactErrorType.DELETE_CONTACT)
        }) {
            contactDao.deleteContact(contact.value!!)
            eventFinish.postEvent()
        }
    }

}