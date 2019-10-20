package com.yatochk.secure.app.ui.notes

import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment

class NotesFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_notes

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

}