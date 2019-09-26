package com.yatochk.secure.app.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment

class GalleryFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_gallery

    private val viewModel: GalleryViewModel by viewModels { viewModelFactory }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}