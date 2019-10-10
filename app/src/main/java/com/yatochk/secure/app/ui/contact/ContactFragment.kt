package com.yatochk.secure.app.ui.contact

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : BaseFragment() {

    private val viewModel: ContactViewModel by viewModels { viewModelFactory }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override val layoutId = R.layout.fragment_contact

    private lateinit var adapter: ContactRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactRecyclerAdapter()
        recycler_contact.layoutManager = LinearLayoutManager(activity!!)
        recycler_contact.adapter = adapter
        observers()
    }

    private fun observers() {
        with(viewModel) {
            contacts.observe(this@ContactFragment) {
                adapter.submitList(it)
            }
        }
    }

}