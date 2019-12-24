package com.yatochk.secure.app.ui.contact

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.getbase.floatingactionbutton.FloatingActionButton
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment
import com.yatochk.secure.app.utils.clear
import com.yatochk.secure.app.utils.hideKeyboard
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.toContact
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : BaseFragment() {

    companion object {
        private const val PICK_CONTACT = 2
    }

    private val viewModel: ContactViewModel by viewModels { viewModelFactory }

    private val contactMenuViewModel: ContactMenuViewModel by viewModels { viewModelFactory }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override val layoutId = R.layout.fragment_contact

    private lateinit var adapter: ContactRecyclerAdapter

    private val newContactButton by lazy {
        FloatingActionButton(activity!!).apply {
            setIcon(R.drawable.ic_new_contact)
            setOnClickListener {
                contactMenuViewModel.clickNewContact()
            }
            colorNormal = ContextCompat.getColor(activity!!, R.color.colorAccent)
            colorPressed = ContextCompat.getColor(activity!!, R.color.colorAccent)
        }
    }
    private val importContactButton by lazy {
        FloatingActionButton(activity!!).apply {
            setIcon(R.drawable.ic_agenda)
            setOnClickListener {
                contactMenuViewModel.clickImportContact()
            }
            colorNormal = ContextCompat.getColor(activity!!, R.color.colorAccent)
            colorPressed = ContextCompat.getColor(activity!!, R.color.colorAccent)
        }
    }

    private fun contactFloatingMenu() {
        floating_menu_contact.addButton(importContactButton)
        floating_menu_contact.addButton(newContactButton)
    }

    private lateinit var contacts: ConstraintSet
    private lateinit var newContact: ConstraintSet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contacts = ConstraintSet()
        contacts.clone(root_contact)
        newContact = ConstraintSet()
        newContact.clone(activity, R.layout.fragment_new_contact)
        contactFloatingMenu()
        adapter = ContactRecyclerAdapter { card, contact ->
            viewModel.clickContact(card, contact)
        }
        recycler_contact.layoutManager = LinearLayoutManager(activity!!)
        recycler_contact.adapter = adapter
        button_save_contact.setOnClickListener {
            viewModel.saveContact(
                new_contact_name.text.toString(),
                new_contact_number.text.toString()
            )
        }
        button_cancel_contact.setOnClickListener {
            viewModel.cancelNewContact()
        }
        observers()
    }

    private fun animateNewContact(showNew: Boolean) {
        TransitionManager.beginDelayedTransition(root_contact)
        (if (showNew) newContact else contacts).applyTo(root_contact)
    }

    private fun observers() {
        initContactMenuObservers()
        with(viewModel) {
            contacts.observe(this@ContactFragment) {
                adapter.submitList(it)
            }
            openContact.observe(this@ContactFragment) {
                val bundle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions.makeSceneTransitionAnimation(
                        activity,
                        it.first,
                        getString(R.string.contact_transition)
                    ).toBundle()
                } else {
                    null
                }
                startActivity(ContactActivity.intent(activity!!, it.second), bundle)
            }
            hideNewContact.observe(this@ContactFragment) {
                new_contact_name.clear()
                new_contact_number.clear()
                activity?.hideKeyboard()
                animateNewContact(false)
            }
        }
    }

    private fun initContactMenuObservers() {
        with(contactMenuViewModel) {
            openContacts.observe(activity!!) {
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                }
                startActivityForResult(intent, PICK_CONTACT)
            }
            openCreateContact.observe(activity!!) {
                floating_menu_contact.collapse()
                recycler_contact.scrollToPosition(adapter.itemCount - 1)
                animateNewContact(true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == PICK_CONTACT) {
            data?.data?.toContact(activity!!)?.also {
                viewModel.receivedContact(it)
            }
        }
    }

}