package com.yatochk.secure.app.ui.notes

import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity

class NoteActivity : BaseActivity() {

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

}
