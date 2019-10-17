package com.yatochk.secure.app.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.model.database.dao.ContactDao
import com.yatochk.secure.app.ui.main.ContactErrorType
import com.yatochk.secure.app.utils.postEvent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditContactViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mutableContact = MutableLiveData<Contact>()
    val contact: LiveData<Contact> = mutableContact

    private val eventFinish = LiveEvent<Void>()
    val finish: LiveData<Void> = eventFinish

    private val eventCall = LiveEvent<String>()
    val call: LiveData<String> = eventCall

    private val eventError = LiveEvent<ContactErrorType>()
    val error: LiveData<ContactErrorType> = eventError

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
        viewModelScope.launch {
            contactDao.updateContact(updated)
        }

    fun delete() {
        viewModelScope.launch {
            contactDao.deleteContact(contact.value!!)
            eventFinish.postEvent()
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}