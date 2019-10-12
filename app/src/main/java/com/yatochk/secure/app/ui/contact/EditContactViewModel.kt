package com.yatochk.secure.app.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.model.database.dao.ContactDao
import com.yatochk.secure.app.ui.main.ContactErrorType
import com.yatochk.secure.app.utils.postEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    fun delete() {
        compositeDisposable.add(Observable.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                contactDao.deleteContact(contact.value!!)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    eventFinish.postEvent()
                },
                {
                    eventError.value = ContactErrorType.DELETE_CONTACT
                }
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}