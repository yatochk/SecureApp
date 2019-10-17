package com.yatochk.secure.app.ui.contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.contact.Contact
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.ui.main.ContactErrorType
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.onDone
import com.yatochk.secure.app.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_contact.*


class ContactActivity : BaseActivity() {

    companion object {
        private const val CONTACT = "contact"

        fun intent(context: Context, contact: Contact) =
            Intent(context, ContactActivity::class.java).apply {
                putExtra(CONTACT, contact)
            }

    }

    private val viewModel: EditContactViewModel by viewModels { viewModelFactory }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        (intent.getSerializableExtra(CONTACT) as? Contact)?.also {
            viewModel.initContact(it)
        }
        container_contact.setOnClickListener { onBackPressed() }
        button_call_contact.setOnClickListener {
            viewModel.call()
        }
        button_delete_contact.setOnClickListener {
            viewModel.delete()
        }
        text_contact_name.setOnFocusChangeListener { _, b ->
            text_contact_name.isCursorVisible = b
        }
        text_contact_number.setOnFocusChangeListener { _, b ->
            text_contact_number.isCursorVisible = b
        }
        text_contact_number.onDone {
            viewModel.editNumber(it)
        }
        text_contact_name.onDone {
            viewModel.editName(it)
        }
        observers()
    }

    private fun observers() {
        with(viewModel) {
            finish.observe(this@ContactActivity) {
                finish()
            }
            call.observe(this@ContactActivity) {
                startActivity(
                    Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", it, null))
                )
            }
            contact.observe(this@ContactActivity) {
                text_contact_name.setText(it.name)
                text_contact_number.setText(it.number)
            }
            error.observe(this@ContactActivity) {
                showErrorToast(
                    this@ContactActivity,
                    when (it) {
                        ContactErrorType.DELETE_CONTACT -> {
                            getString(R.string.error_delete_contact)
                        }
                    }
                )
            }
        }
    }

}